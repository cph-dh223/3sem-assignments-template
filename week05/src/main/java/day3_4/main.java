package day3_4;

import day3_4.config.HibernateConfig;
import day3_4.controlers.HotelController;
import day3_4.controlers.RoomController;
import day3_4.daos.HotelDAO;
import day3_4.daos.RoomDAO;
import day3_4.ressources.Hotel;
import day3_4.ressources.Room;
import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

import com.fasterxml.jackson.databind.ObjectMapper;

public class main {
    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig("hoteldb", false);
        Javalin app = Javalin.create().start(7070);
        app.routes(getResourses(emf));
        app.error(404, ctx -> {
            String message = ctx.attribute("msg");
            message = "{\"err msg\": \"" + message + "\"}";
            ctx.json(message);
        });
        app.exception(IllegalStateException.class, (e, ctx) -> {
            ObjectMapper mapper = new ObjectMapper();
            var statusCode = 422;
            var node = mapper.createObjectNode()
                        .put("status", statusCode)
                        .put("msg", e.getMessage());
            ctx.json(node);
            ctx.status(statusCode);
        });
        // setup(emf);
    }
    private static EndpointGroup getResourses(EntityManagerFactory emf){
        HotelDAO hotelDAO = new HotelDAO(emf);
        RoomDAO roomDAO = new RoomDAO(emf);
        HotelController hotelController = new HotelController(hotelDAO);
        RoomController roomController = new RoomController(roomDAO);
        return  () -> {
            get("/",ctx -> ctx.result("Hello World"));
            path("/hotel", () ->{
                get(hotelController.getAll());
                post(hotelController.update());
                path("/{id}", () -> {
                    get(hotelController.getById());
                    put(hotelController.create());
                    delete(hotelController.delete());
                    get("/rooms", hotelController.getHotelRooms());
                });
            });
            path("/room", () -> {
                get(roomController.getAll());
                post(roomController.create());
                path("/{id}", () -> {
                    get(roomController.getById());
                    put(roomController.update());
                    delete(roomController.delete());
                });
            });
        };
    }

    private static void setup(EntityManagerFactory emf){
        Hotel h1 = new Hotel("h1", "Street 1");
        Hotel h2 = new Hotel("h2", "Street 2");
        Hotel h3 = new Hotel("h3", "Street 3");
        Room r1_1 = new Room(1, 100f);
        Room r1_2 = new Room(2, 1000f);
        Room r2_1 = new Room(1, 100f);
        Room r2_2 = new Room(2, 1000f);
        Room r3_1 = new Room(1, 100f);
        Room r3_2 = new Room(2, 1000f);

        h1.addRoom(r1_1);
        h1.addRoom(r1_2);
        h2.addRoom(r2_1);
        h2.addRoom(r2_2);
        h3.addRoom(r3_1);
        h3.addRoom(r3_2);
        HotelDAO hotelDAO = new HotelDAO(emf);
        RoomDAO roomDAO = new RoomDAO(emf);

        hotelDAO.create(h1);
        hotelDAO.create(h2);
        hotelDAO.create(h3);

        roomDAO.create(r1_1);
        roomDAO.create(r1_2);
        roomDAO.create(r2_1);
        roomDAO.create(r2_2);
        roomDAO.create(r3_1);
        roomDAO.create(r3_2);

    }

}
