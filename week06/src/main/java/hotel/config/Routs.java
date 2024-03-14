package hotel.config;


import com.fasterxml.jackson.databind.ObjectMapper;

import hotel.controlers.HotelController;
import hotel.controlers.RoomController;
import hotel.controlers.SecurityController;
import hotel.daos.HotelDAO;
import hotel.daos.RoomDAO;
import hotel.daos.UserDAO;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.EntityManagerFactory;
import io.javalin.security.RouteRole;


import static io.javalin.apibuilder.ApiBuilder.*;



public class Routs {
    public static EndpointGroup getResourses(EntityManagerFactory emf){
        HotelDAO hotelDAO = new HotelDAO(emf);
        RoomDAO roomDAO = new RoomDAO(emf);
        HotelController hotelController = new HotelController(hotelDAO);
        RoomController roomController = new RoomController(roomDAO);
        SecurityController userController = SecurityController.getInstance();
        return  () -> {
            get("/",ctx -> ctx.result(new ObjectMapper().writeValueAsString("Hello World")));
            path("/hotel", () ->{
                get(hotelController.getAll());
                path("/{id}", () -> {
                    post(hotelController.update());
                    get(hotelController.getById());
                    put(hotelController.create());
                    delete(hotelController.delete());
                    get("/rooms", hotelController.getHotelRooms());
                });
            });
            path("/room", () -> {
                get(roomController.getAll());
                path("/{id}", () -> {
                    post(roomController.create());
                    get(roomController.getById());
                    put(roomController.update());
                    delete(roomController.delete());
                });
            });
            path("/user", () ->{
                post(userController.create(), Role.ANYONE);
                post("/login", userController.login(), Role.ANYONE);
                path("/user", () -> {
                    before(userController.authenticateUser());
                    get(userController.getUserRoles(), Role.USER);
                });
                path("/admin", () -> {
                    before(userController.authenticateAdmin());
                    get("/all", userController.getAll(), Role.ADMIN);
                });
            });
        };
    }
    public enum Role implements RouteRole {
        ANYONE, 
        USER, 
        ADMIN 
    }
}
