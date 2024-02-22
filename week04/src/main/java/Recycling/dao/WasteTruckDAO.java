package Recycling.dao;

import java.util.List;

import Recycling.modle.Driver;
import Recycling.modle.WasteTruck;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

public class WasteTruckDAO extends absDAO implements IWasteTruckDAO{

    public WasteTruckDAO(EntityManagerFactory emf) {
        super(emf);
    }

    @Override
    public void saveWasteTruck(String brand, String registrationNumber, int capacity) {
        WasteTruck truck = new WasteTruck(brand, capacity, registrationNumber);
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(truck);
            em.getTransaction().commit();
        }
    }

    @Override
    public WasteTruck getWasteTruckById(int id) {
        try(EntityManager em = emf.createEntityManager()){
            return em.find(WasteTruck.class, id);
        }    
    }

    @Override
    public void setWasteTruckAvailable(WasteTruck wasteTruck, boolean available) {
        wasteTruck.setAvailable(available);
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(wasteTruck);
            em.getTransaction().commit();
        }
    }

    @Override
    public void deleteWasteTruck(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.remove(getWasteTruckById(id));
            em.getTransaction().commit();
        }
    }

    @Override
    public void addDriverToWasteTruck(WasteTruck wasteTruck, Driver driver) {
        wasteTruck.addDriver(driver);
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(wasteTruck);
            em.getTransaction().commit();
        }
    }

    @Override
    public void removeDriverFromWasteTruck(WasteTruck wasteTruck, String id) {
        wasteTruck.removeDriverById(id);
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(wasteTruck);
            em.getTransaction().commit();
        }
    }

    @Override
    public List<WasteTruck> getAllAvailableTrucks() {
        try (EntityManager em = emf.createEntityManager()) {
            String sql = "FROM WasteTruck t WHERE t.is_available";
            TypedQuery<WasteTruck> q = em.createQuery(sql, WasteTruck.class);
            return q.getResultList();
        }
    }

    
}
