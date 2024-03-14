package hotel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import hotel.config.ApplicationConfig;
import hotel.config.HibernateConfig;
import hotel.config.Routs;
import hotel.ressources.Hotel;
import hotel.ressources.Room;
import io.javalin.http.ContentType;
import io.javalin.http.HttpStatus;
import io.restassured.RestAssured;
import static org.hamcrest.Matchers.*;
import io.restassured.response.Response;
import jakarta.persistence.EntityManagerFactory;
import static io.restassured.RestAssured.*;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/*
 * Tests run individualy work but as a group they brake
 */
public class EndpointTest {

    private static ApplicationConfig appConfig;
    private static final String BASE_URL = "http://localhost:7777";
    private static EntityManagerFactory emfTest;
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static Map<Integer,Hotel> hotels;
    private static Map<Integer,Room> rooms;
    @BeforeAll
    public static void beforeAll(){
        RestAssured.baseURI = BASE_URL;
        objectMapper.findAndRegisterModules();
        HibernateConfig.setTestMode(true);
        

        // Setup test database using docker testcontainers
        emfTest = HibernateConfig.getEntityManagerFactoryForTest();

        // Start server
        appConfig = ApplicationConfig.
                getInstance()
                .initiateServer()
                .setGeneralExceptionHandling()
                .setRoutes(Routs.getResourses(emfTest))
                .setCORS()
                .startServer(7777)
        ;
    }

    @BeforeEach
    public void setUpEach() {
        // Setup test database for each test
        new TestUtils().createHotelsAndRooms(emfTest);
        hotels = new TestUtils().getHotels(emfTest);
        rooms = new TestUtils().getRooms(emfTest);
        
    }
    
    @AfterAll
    static void afterAll() {
        HibernateConfig.setTestMode(false);
        appConfig.stopServer();
    }
    
    @Test
    public void defTest(){
        given().when().get("/").peek().then().statusCode(200);
    }
    
    @Test
    public void getAllHotels() throws JsonMappingException, JsonProcessingException {
        
        Response res = given().when().get("/hotel").peek();
        
        Hotel[] response = objectMapper.readValue(res.asString(), Hotel[].class);
        assertEquals(hotels.size(), response.length);
    }

    @Test
    public void updateHotel() throws JsonProcessingException{
        Hotel h = hotels.get(hotels.keySet().toArray()[0]);
        h.setName("new h1");
        String requestBody = objectMapper.writeValueAsString(h);
        given()
            .header("Content-type", ContentType.JSON)
            .body(requestBody)
            .when()
            .post("/hotel/" + h.getId())
            .then()
            .log().body()
            .assertThat()
            .statusCode(200)
            .body("name", equalTo(h.getName()))
            .body("address", equalTo(h.getAddress()));
    }
    
    @Test
    public void getHotelById() {
        Hotel h = hotels.get(hotels.keySet().toArray()[0]);
        given().when().get("hotel/" + h.getId()).peek().then().assertThat().statusCode(200).body("name",equalTo(h.getName()));
    }

    @Test
    public void createHotel() throws JsonProcessingException{
        Hotel h = new Hotel("h4","a4");
        
        String requestBody = objectMapper.writeValueAsString(h);
        given()
            .header("Content-type", ContentType.JSON)
            .body(requestBody)
            .when()
            .put("/hotel/" + h.getId())
            .then()
            .assertThat()
            .statusCode(201)
            .body("name", equalTo(h.getName()))
            .body("address", equalTo(h.getAddress()));
    }

    @Test
    public void deleteHotel() throws JsonProcessingException{
        Hotel h = hotels.get(hotels.keySet().toArray()[0]);
        
        String requestBody = objectMapper.writeValueAsString(h);
        given()
            .header("Content-type", ContentType.JSON)
            .body(requestBody)
            .when()
            .delete("/hotel/" + h.getId())
            .then()
            .assertThat()
            .statusCode(200);
    }
    

    @Test
    public void getHotelRooms() throws JsonProcessingException{
        Hotel h = hotels.get(hotels.keySet().toArray()[0]);
        
        String requestBody = objectMapper.writeValueAsString(h);

        
        var res = given()
            .header("Content-type", ContentType.JSON)
            .body(requestBody)
            .when()
            .get("/hotel/" + h.getId() + "/rooms");
        
        Room[] response = objectMapper.readValue(res.asString(), Room[].class);
        assertEquals(h.getRooms().size(), response.length);
        
        for (Room room : response) {
            assertTrue(h.getRooms().stream().filter(r -> r.getId() == room.getId()).collect(Collectors.toList()).size() == 1);
        }
    }

    @Test
    public void getAllRooms() throws JsonMappingException, JsonProcessingException {
        
        Response res = given().when().get("/room").peek();
        
        Room[] response = objectMapper.readValue(res.asString(), Room[].class);
        assertEquals(rooms.size(), response.length);
    }

    @Test
    public void updateRoom() throws JsonProcessingException{
        Room r = rooms.get(rooms.keySet().toArray()[0]);
        r.setPrice(20000f);
        String requestBody = objectMapper.writeValueAsString(r);
        r.getHotel();
        given()
            .header("Content-type", ContentType.JSON)
            .body(requestBody)
            .when()
            .post("/room/" + r.getId())
            .then()
            .log().body()
            .assertThat()
            .statusCode(200)
            .body("roomNumber", equalTo("" + r.getRoomNumber()))
            .body("price", equalTo("" + r.getPrice()));
    }
    
    @Test
    public void getRoomById() {
        Room r = rooms.get(rooms.keySet().toArray()[0]);
        given().when().get("room/" + r.getId()).peek().then().assertThat().statusCode(200).body("roomNumber",equalTo(r.getRoomNumber()));
    }

    @Test
    public void createRoom() throws JsonProcessingException{
        Hotel h = hotels.get(hotels.keySet().toArray()[0]);
        Room r = new Room(300, h);
        r.setPrice(20);
        String requestBody = objectMapper.writeValueAsString(r);
        given()
            .header("Content-type", ContentType.JSON)
            .body(requestBody)
            .when()
            .put("/room/" + r.getId())
            .then()
            .assertThat()
            .statusCode(200)
            .body("roomNumber", equalTo(r.getRoomNumber()))
            .body("price", equalTo(r.getPrice()));
    }

    @Test
    public void deleteRoom() throws JsonProcessingException{
        Room r = rooms.get(rooms.keySet().toArray()[0]);
        
        String requestBody = objectMapper.writeValueAsString(r);
        given()
            .header("Content-type", ContentType.JSON)
            .body(requestBody)
            .when()
            .delete("/room/" + r.getId())
            .then()
            .assertThat()
            .statusCode(200);
    }
}
