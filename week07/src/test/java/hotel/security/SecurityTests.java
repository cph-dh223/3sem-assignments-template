package hotel.security;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hotel.TestUtils;
import hotel.config.ApplicationConfig;
import hotel.config.HibernateConfig;
import hotel.config.Routs;
import hotel.controlers.SecurityController;
import hotel.daos.UserDAO;
import hotel.dtos.TokenDTO;
import hotel.dtos.UserDTO;
import io.restassured.RestAssured;
import io.restassured.http.Header;

import static org.hamcrest.Matchers.*;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import jakarta.persistence.EntityManagerFactory;
import static io.restassured.RestAssured.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SecurityTests {
    private static ApplicationConfig appConfig;
    private static final String BASE_URL = "http://localhost:7777/api";
    private static EntityManagerFactory emfTest;
    private static ObjectMapper objectMapper = new ObjectMapper();
    
    @BeforeAll
    public static void beforeAll(){
        RestAssured.baseURI = BASE_URL;
        objectMapper.findAndRegisterModules();
        HibernateConfig.setTestMode(true);
        

        // Setup test database using docker testcontainers
        emfTest = HibernateConfig.getEntityManagerFactoryForTest();

        // Start server
        appConfig = ApplicationConfig.getInstance(emfTest);
        appConfig
                .initiateServer()
                .setExceptionHandling()
                .checkSecurityRoles()
                .setRoute(Routs.getResourses(emfTest))
                .setRoute(Routs.getSecurityRoutes(emfTest))
                .setRoute(Routs.securedRoutes(emfTest))
                .startServer(7777)
            ;
    }

    @BeforeEach
    public void setUpEach() {
        // Setup test database for each test
        new TestUtils().createUsersAndRoles(emfTest);
        
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
        ResponseBody res = given()
            .body(requestBody)
        .when()
            .post("/auth/register")
            .peek()
            .body();
            
        
        String body = res.asString();
        String token = body.split(",")[0].split(":")[1].replace("\"", "");
        UserDTO newUser = (new SecurityController(new UserDAO(emfTest))).verifyToken(token);
        assertNotNull(newUser);
        assertEquals("test1", newUser.getUsername());
    }

    @Test
    public void login() {
        String requestBody = "{\"username\": \"user\",\"password\": \"user\"}";
        given()
            .body(requestBody)
        .when()
            .post("/auth/login")
        .then()
            .log().body()
            .assertThat()
            .statusCode(200)
            .body("userName", equalTo("user"));
        ;
    }
    
    @Test
    public void protectedUser() throws JsonMappingException, JsonProcessingException {
        String requestBody = "{\"username\": \"user\",\"password\": \"user\"}";
        Response res =
            given()
                .body(requestBody)
            .when()
                .post("/auth/login");

        TokenDTO token = objectMapper.readValue(res.body().asString(), TokenDTO.class);
        Header header = new Header("Authorization", "Bearer " + token.getToken());
        
        given()
            .header(header)
        .when()
            .get("/protected/user_demo")
        .then()
            .log().body()
            .assertThat()
            .statusCode(200)
            .body("msg", equalTo("Hello from USER Protected"))
            ;
        
    }

    @Test
    public void userTryesToAccessAdmin() throws JsonMappingException, JsonProcessingException {
        String requestBody = "{\"username\": \"user\",\"password\": \"user\"}";
        Response res =
            given()
                .body(requestBody)
            .when()
                .post("/auth/login")
                .peek();
                

        TokenDTO token = objectMapper.readValue(res.body().asString(), TokenDTO.class);
        Header header = new Header("Authorization", "Bearer " + token.getToken());
        
        given()
            .header(header)
        .when()
            .get("/protected/admin_demo")
        .then()
            .log().headers()
            .log().body()
            .assertThat()
            .statusCode(403)
            .body("errrorMessage", equalTo("Unauthorized with roles: [USER]"));
    }

    @Test
    public void protectedAdmin() throws JsonMappingException, JsonProcessingException {
        String requestBody = "{\"username\": \"admin\",\"password\": \"admin\"}";
        Response res =
            given()
                .body(requestBody)
            .when()
                .post("/auth/login");

        TokenDTO token = objectMapper.readValue(res.body().asString(), TokenDTO.class);
        Header header = new Header("Authorization", "Bearer " + token.getToken());
        
        given()
            .header(header)
        .when()
            .get("/protected/admin_demo")
        .then()
            .log().body()
            .assertThat()
            .statusCode(200)
            .body("msg", equalTo("Hello from ADMIN Protected"))
            ;
            
        
    
    }
    
    
    
}
