package day3_4.daos;

import java.util.List;

import day3_4.ressources.Room;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

public class RoomDAO extends ADAO<Room>{

    public RoomDAO(EntityManagerFactory emf) {
        super(emf);
    }

    @Override
    public List<Room> getAll() {
        try(var em = emf.createEntityManager()){
            TypedQuery<Room> q = em.createNamedQuery("FROM room", Room.class);
            return q.getResultList();
        }
    }
    
    @Override
    public Room getById(int id) {
        try(var em = emf.createEntityManager()){
            TypedQuery<Room> q = em.createNamedQuery("FROM Room WHERE id = :id", Room.class);
            q.setParameter("id", id);
            return q.getSingleResult();
        }
    }
    
    @Override
    public Room update(Room room) {
        try(var em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.merge(room);
            em.getTransaction().commit();
        }
        return room;
    }
    
}
