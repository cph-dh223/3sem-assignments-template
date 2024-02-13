package GLS;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

public class PackageDAO {

    private EntityManager em;
    public PackageDAO(){
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig();
        em = emf.createEntityManager();
    }
    public Package persistPackage(Package p){
        em.getTransaction().begin();
        em.persist(p);
        em.getTransaction().commit();
        return p;
    }
    
    public Package readPakage(int id){
        Package p = em.find(Package.class, id);
        return p;
    }
    public Package readPakageFromTrackingNumber(String trackingNumber){
        Package readPackage = null;
        String sql = "FROM Package";
        TypedQuery<Package> packeges = em.createQuery(sql, Package.class);
        readPackage = packeges.getResultList().get(0);
        return readPackage;
    }
    
    public Package updatePakage(Package pakage){
        em.getTransaction().begin();
        Package updStd = em.merge(pakage);
        em.getTransaction().commit();
        return updStd;
    }
    
    public void deletePackage(int id){
        em.getTransaction().begin();
        Package p = readPakage(id);
        if (p != null){
            em.remove(p);
        }
        em.getTransaction().commit();
    }
    
    public void close(){
        em.close();
    }
}
