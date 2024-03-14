package hotel;

import hotel.config.HibernateConfig;
import hotel.config.Routs;
import hotel.controlers.HotelController;
import hotel.controlers.RoomController;
import hotel.controlers.SecurityController;
import hotel.daos.HotelDAO;
import hotel.daos.RoomDAO;
import hotel.daos.UserDAO;
import hotel.ressources.Hotel;
import hotel.ressources.Role;
import hotel.ressources.Room;
import hotel.ressources.User;
import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;


import com.fasterxml.jackson.databind.ObjectMapper;

public class main {
    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        Javalin app = Javalin.create().start(7070);
        app.routes(Routs.getResourses(emf));
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
        setup(emf);
    }
    

    private static void setup(EntityManagerFactory emf){
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Room r").executeUpdate();
            em.createQuery("DELETE FROM Hotel h").executeUpdate();
            em.createQuery("DELETE FROM User u").executeUpdate();
            em.createQuery("DELETE FROM Role r").executeUpdate();
            
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
                
            em.persist(h1);
            em.persist(h2);
            em.persist(h3);
            em.persist(r1_1);
            em.persist(r1_2);
            em.persist(r2_1);
            em.persist(r2_2);
            em.persist(r3_1);
            em.persist(r3_2);

            User admin = new User("admin", "1234");
            Role adminRole = new Role("admin");
            admin.addRole(adminRole);

            em.persist(admin);
            em.persist(adminRole);

            em.getTransaction().commit();
        }
    }

}
