package hotel.security;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
    
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import hotel.TestUtils;
import hotel.config.ApplicationConfig;
import hotel.config.HibernateConfig;
import hotel.config.Routs;
import hotel.ressources.Hotel;
import hotel.ressources.Role;
import hotel.ressources.Room;
import hotel.ressources.User;
import io.javalin.http.ContentType;
import io.javalin.http.HttpStatus;
import io.restassured.RestAssured;
import io.restassured.http.Header;

import static org.hamcrest.Matchers.*;
import io.restassured.response.Response;
import jakarta.persistence.EntityManagerFactory;
import static io.restassured.RestAssured.*;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import java.util.stream.Collectors;

public class SecurityTests {
    private static ApplicationConfig appConfig;
    private static final String BASE_URL = "http://localhost:7777";
    private static EntityManagerFactory emfTest;
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static Map<String,User> users;
    private static Map<String,Role> roles;
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
                .checkSecurityRoles()
                .setCORS()
                .startServer(7777)
        ;
    }

    @BeforeEach
    public void setUpEach() {
        // Setup test database for each test
        new TestUtils().createUsersAndRoles(emfTest);
        users = new TestUtils().getUsers(emfTest);
        roles = new TestUtils().getRoles(emfTest);
        
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
    public void createUser() {
        String requestBody = "{\"username\": \"test1\",\"password\": \"test1\"}";
        given()
            .body(requestBody)
        .when()
            .post("/user")
        .then()
            .assertThat()
            .statusCode(201)
            .header("Access-Control-Allow-Headers", "Content-Type, Authorization")
            .body(equalTo("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0MSIsInVzZXJOYW1lIjoidGVzdDEiLCJyb2xlcyI6IiJ9.YWSVa22sEg633JeRQczzp22K7_fjJOWDJYdqL25NrBE"))
            ; 
    }

    @Test
    public void login() {
        String requestBody = "{\"username\": \"user\",\"password\": \"user\"}";
        given()
            .body(requestBody)
        .when()
            .post("/user/login")
        .then()
            .log().body()
            .assertThat()
            .statusCode(202)
            .header("Access-Control-Allow-Headers", "Content-Type, Authorization")
            .body(equalTo("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwidXNlck5hbWUiOiJ1c2VyIiwicm9sZXMiOiIsdXNlciJ9.RpcOdjPYHtj2f-jMdxzp2t8dRHlNRFeja3rzQJFaEp0"));
        ;
    }
    
    @Test
    public void userGetsRoles() {
        String requestBody = "{\"username\": \"user\",\"password\": \"user\"}";
        Response res =
            given()
                .body(requestBody)
            .when()
                .post("/user/login");

        String auth = res.body().asString();
        Header header = new Header("Authorization", "Bearer " + auth);
        res =
            given()
                .header(header)
            .when()
                .get("/user/user");
        
        assertEquals(200, res.statusCode());
        assertTrue(res.asString().contains("user"));       
    }

    @Test
    public void userTryesToGetAllUsers() {
        String requestBody = "{\"username\": \"user\",\"password\": \"user\"}";
        Response res =
            given()
                .body(requestBody)
            .when()
                .post("/user/login");

        String auth = res.body().asString();
        Header header = new Header("Authorization", "Bearer " + auth);
        res =
            given()
                .header(header)
            .when()
                .get("/user/admin/all");
    
        assertEquals(403, res.statusCode());
        assertEquals("{\"msg\":\"Not authorized. No username were added from the token\"}", res.asString());
    }

    @Test
    public void adminGetsAllUsers() {
        String requestBody = "{\"username\": \"admin\",\"password\": \"admin\"}";
        Response res =
            given()
                .body(requestBody)
            .when()
                .post("/user/login");

        String auth = res.body().asString();
        Header header = new Header("Authorization", "Bearer " + auth);
        res =
            given()
                .header(header)
            .when()
                .get("/user/admin/all");
            
        assertEquals(200, res.statusCode());
        assertTrue(res.asString().contains("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJOYW1lIjoiYWRtaW4iLCJyb2xlcyI6IixhZG1pbiJ9.Fz3LTZHobs0jtnxUw-YFJQ8cU_MMg6zA3YJuvEIbhXA"));
        assertTrue(res.asString().contains("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwidXNlck5hbWUiOiJ1c2VyIiwicm9sZXMiOiIsdXNlciJ9.RpcOdjPYHtj2f-jMdxzp2t8dRHlNRFeja3rzQJFaEp0"));
    
    }
    
    
    
}
