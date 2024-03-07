package day3_4.daos;

import java.util.List;

import day3_4.ressources.Hotel;
import day3_4.ressources.Room;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

public class HotelDAO extends ADAO<Hotel>{

    public HotelDAO(EntityManagerFactory emf) {
        super(emf);
    }

    @Override
    public List<Hotel> getAll() {
        try(var em = emf.createEntityManager()){
            TypedQuery<Hotel> q = em.createQuery("FROM Hotel h", Hotel.class);
            List<Hotel> hotels = q.getResultList();
            // hotels.forEach(h -> {
            //     TypedQuery<Room> roomQuery = em.createQuery("SELECT r From Hotel h JOIN h.rooms r WHERE h.id = :id", Room.class);
            //     roomQuery.setParameter("id", h.getId());
            //     roomQuery.getResultList().forEach(r -> h.addRoom(r));
            // });
            
            return hotels;
        }
    }
    
    @Override
    public Hotel getById(int id) {
        try(var em = emf.createEntityManager()){
            TypedQuery<Hotel> q = em.createQuery("FROM Hotel h WHERE h.id = :id", Hotel.class);
            q.setParameter("id", id);
            return q.getSingleResult();
        }
    }
    
    @Override
    public Hotel update(Hotel hotel) {
        try(var em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.merge(hotel);
            em.getTransaction().commit();
        }
        return hotel;
    }
    
}
