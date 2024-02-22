package Recycling.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import Recycling.modle.Driver;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

public class DriverDAO extends absDAO implements IDriverDAO{

    public DriverDAO(EntityManagerFactory emf) {
        super(emf);
    }

    @Override
    public void saveDriver(String name, String surname, BigDecimal salary) {
        Driver driver = new Driver(name, surname, salary);
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(driver);
            em.getTransaction().commit();
        }
    }

    @Override
    public Driver getDriverById(String id) {
        try(EntityManager em = emf.createEntityManager()){
            return em.find(Driver.class, id);
        }
    }

    @Override
    public Driver updateDriver(Driver driver) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            driver = em.merge(driver);
            em.getTransaction().commit();
            return driver;
        } 
    }
    
    @Override
    public void deleteDriver(String id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.remove(getDriverById(id));
            em.getTransaction().commit();
        }
    }
    
    @Override
    public List<Driver> findAllDriversEmployedAtTheSameYear(String year) {
        try (EntityManager em = emf.createEntityManager()) {
            String sql = "FROM Driver";
            TypedQuery<Driver> q = em.createQuery(sql, Driver.class);
            return q.getResultStream().filter(a -> Integer.toString(a.getEmploymentDate().getYear()).equals(year)).collect(Collectors.toList());
        }
    }
    
    @Override
    public List<Driver> fetchAllDriversWithSalaryGreaterThan10000() {
        try (EntityManager em = emf.createEntityManager()) {
            String sql = "FROM Driver d WHERE d.salary > 1000";
            TypedQuery<Driver> q = em.createQuery(sql, Driver.class);
            return q.getResultList();
        }
    }
    
    @Override
    public BigDecimal fetchHighestSalary() {
        try (EntityManager em = emf.createEntityManager()) {
            String sql = "SELECT d.salary FROM Driver d";
            TypedQuery<BigDecimal> q = em.createQuery(sql, BigDecimal.class);
            return q.getResultStream().reduce((acc, b) -> acc = acc.compareTo(b) > 0 ? acc : b).get();
        }
    }
    
    @Override
    public List<String> fetchFirstNameOfAllDrivers() {
        try (EntityManager em = emf.createEntityManager()) {
            String sql = "SELECT d.name FROM Driver d";
            TypedQuery<String> q = em.createQuery(sql, String.class);
            return q.getResultList();            
        }
    }
    
    @Override
    public long calculateNumberOfDrivers() {
        try (EntityManager em = emf.createEntityManager()) {
            String sql = "SELECT COUNT(d) FROM Driver d";
            TypedQuery<Long> q = em.createQuery(sql, Long.class);
            return q.getSingleResult();   
        }
    }
    
    @Override
    public Driver fetchDriverWithHighestSalary() {
        try (EntityManager em = emf.createEntityManager()) {
            String sql = "SELECT d FROM Driver d ORDER BY d.salary DESC";
            TypedQuery<Driver> q = em.createQuery(sql, Driver.class);
            return q.getResultList().get(0);
        }
       
    }
    
}
