package hotel.daos;

import java.util.List;

import hotel.ressources.Hotel;
import hotel.ressources.Room;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

public class HotelDAO extends ADAO<Hotel>{

    public HotelDAO(EntityManagerFactory emf) {
        super(emf);
    }

    @Override
    public List<Hotel> getAll() {
        try(var em = emf.createEntityManager()){
            TypedQuery<Hotel> q = em.createQuery("SELECT h FROM Hotel h", Hotel.class);
            List<Hotel> hotels = q.getResultList();            
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
