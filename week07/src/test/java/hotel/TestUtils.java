package hotel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import static io.javalin.apibuilder.ApiBuilder.delete;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;
import static io.javalin.apibuilder.ApiBuilder.put;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.mail.Address;

import com.fasterxml.jackson.databind.ObjectMapper;

import hotel.controlers.HotelController;
import hotel.controlers.RoomController;
import hotel.daos.HotelDAO;
import hotel.daos.RoomDAO;
import hotel.daos.UserDAO;
import hotel.ressources.Hotel;
import hotel.ressources.Role;
import hotel.ressources.Room;
import hotel.ressources.User;
import io.javalin.apibuilder.EndpointGroup;

public class TestUtils {
    public void createHotelsAndRooms(EntityManagerFactory emfTest) {
        try (EntityManager em = emfTest.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Room r").executeUpdate();
            em.createQuery("DELETE FROM Hotel h").executeUpdate();
            
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
            em.getTransaction().commit();
        }
    }

    public Map<Integer, Hotel> getHotels(EntityManagerFactory emfTest) {
        return new HotelDAO(emfTest).getAll().stream().collect(Collectors.toMap(h -> h.getId(), h -> h));
    }

    public Map<Integer, Room> getRooms(EntityManagerFactory emfTest) {
        return new RoomDAO(emfTest).getAll().stream().collect(Collectors.toMap(r -> r.getId(), r -> r));
    }

    public void createUsersAndRoles(EntityManagerFactory emfTest) {
        try (EntityManager em = emfTest.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM User u").executeUpdate();
            em.createQuery("DELETE FROM Role r").executeUpdate();
            
            User u1 = new User("admin", "admin");
            User u2 = new User("user", "user");

            Role r1 = new Role("admin");
            Role r2 = new Role("user");

            u1.addRole(r1);
            u2.addRole(r2);
                
            em.persist(u1);
            em.persist(u2);
            em.persist(r1);
            em.persist(r2);
            
            em.getTransaction().commit();
        }
    }

    public Map<String, User> getUsers(EntityManagerFactory emfTest) {
        return new UserDAO(emfTest).getAllUsers().stream().collect(Collectors.toMap(u -> u.getUsername(), u -> u));
    }

    public Map<String, Role> getRoles(EntityManagerFactory emfTest) {
        return new UserDAO(emfTest).getAllRoles().stream().collect(Collectors.toMap(r -> r.getName(), r -> r));
    }
    
        
}
