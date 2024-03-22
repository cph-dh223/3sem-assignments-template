package hotel.config;


import com.fasterxml.jackson.databind.ObjectMapper;

import hotel.controlers.HotelController;
import hotel.controlers.ISecurityController;
import hotel.controlers.RoomController;
import hotel.controlers.SecurityController;
import hotel.daos.HotelDAO;
import hotel.daos.RoomDAO;
import hotel.daos.UserDAO;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;
import io.javalin.security.RouteRole;


import static io.javalin.apibuilder.ApiBuilder.*;



public class Routs {
    private static ObjectMapper om = new ObjectMapper();
    public static EndpointGroup getResourses(EntityManagerFactory emf){
        HotelDAO hotelDAO = new HotelDAO(emf);
        RoomDAO roomDAO = new RoomDAO(emf);
        HotelController hotelController = new HotelController(hotelDAO);
        RoomController roomController = new RoomController(roomDAO);
        return  () -> {
            get("/",ctx -> ctx.result(new ObjectMapper().writeValueAsString("Hello World")), Role.ANYONE);
            path("/hotel", () ->{
                get(hotelController.getAll(), Role.ANYONE);
                path("/{id}", () -> {
                    post(hotelController.update(), Role.ANYONE);
                    get(hotelController.getById(), Role.ANYONE);
                    put(hotelController.create(), Role.ANYONE);
                    delete(hotelController.delete(), Role.ANYONE);
                    get("/rooms", hotelController.getHotelRooms(), Role.ANYONE);
                });
            });
            path("/room", () -> {
                get(roomController.getAll(), Role.ANYONE);
                path("/{id}", () -> {
                    post(roomController.create(), Role.ANYONE);
                    get(roomController.getById(), Role.ANYONE);
                    put(roomController.update(), Role.ANYONE);
                    delete(roomController.delete(), Role.ANYONE);
                });
            });
        };
    }

    public static EndpointGroup getSecurityRoutes(EntityManagerFactory emf) {
        ISecurityController securityController = new SecurityController(new UserDAO(emf));
        return ()->{
            path("/auth", ()->{
                post("/login", securityController.login(),Role.ANYONE);
                post("/register", securityController.register(),Role.ANYONE);
            });
        };
    }
    public static EndpointGroup securedRoutes(EntityManagerFactory emf){
        ISecurityController securityController = new SecurityController(new UserDAO(emf));
        return ()->{
            path("/protected", ()->{
                before(securityController.authenticate());
                get("/user_demo",(ctx)->ctx.json(om.createObjectNode().put("msg",  "Hello from USER Protected")),Role.USER);
                get("/admin_demo",(ctx)->ctx.json(om.createObjectNode().put("msg",  "Hello from ADMIN Protected")),Role.ADMIN);
            });
        };
    }
    public enum Role implements RouteRole {
        ANYONE, 
        USER, 
        ADMIN 
    }
}
