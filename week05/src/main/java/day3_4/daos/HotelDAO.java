package day3_4.daos;

import java.util.List;

import day3_4.ressources.Hotel;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

public class HotelDAO extends ADAO<Hotel>{

    public HotelDAO(EntityManagerFactory emf) {
        super(emf);
    }

    @Override
    public List<Hotel> getAll() {
        try(var em = emf.createEntityManager()){
            TypedQuery<Hotel> q = em.createNamedQuery("FROM Hotel", Hotel.class);
            return q.getResultList();
        }
    }
    
    @Override
    public Hotel getById(int id) {
        try(var em = emf.createEntityManager()){
            TypedQuery<Hotel> q = em.createNamedQuery("FROM Hotel WHERE id = :id", Hotel.class);
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
