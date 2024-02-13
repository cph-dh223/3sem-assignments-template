package thePoint;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

public class PointDAO {
    EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig();

	public void createPoints(int count) {

		try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            for (int i = 0; i < count; i++) {
                Point p = new Point(i, i);
                em.persist(p);
            }
            em.getTransaction().commit();
        }
	}
    
    public void numPoints() {
        try(EntityManager em = emf.createEntityManager()){
            Query q1 = em.createQuery("SELECT COUNT(p) FROM Point p");
            System.out.println("Total Points: " + q1.getSingleResult());
        }
    }
    
    public void avgX() {
        try(EntityManager em = emf.createEntityManager()){
            Query q2 = em.createQuery("SELECT AVG(p.x) FROM Point p");
            System.out.println("Average X: " + q2.getSingleResult());
        }
    }
    
    public List<Point> getAllPoints() {
        List<Point> points = new ArrayList<>();
        try(EntityManager em = emf.createEntityManager()){
        
            TypedQuery<Point> query = em.createQuery("SELECT p FROM Point p", Point.class);
            points = query.getResultList();
        }
        return points;
    }
    public void close(){
        emf.close();
    }
    
}
