package Unicorn;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class UniconrDAO {
    private EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig();

    public Unicorn save(Unicorn unicorn){
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(unicorn);
        em.getTransaction().commit();
        em.close();
        return unicorn;
    }

    public Unicorn findById(int id){
        EntityManager em = emf.createEntityManager();
        Unicorn foundUnicorn = em.find(Unicorn.class, id);
        em.close();
        return foundUnicorn;
    }

    public Unicorn update(Unicorn unicorn){
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Unicorn updatedUnicorn = em.merge(unicorn);
        em.getTransaction().commit();
        em.close();
        return updatedUnicorn;
    }

    public void delete(int id){
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Unicorn unicorn = findById(id);
        if (unicorn != null){
            em.remove(unicorn);
        }
        em.getTransaction().commit();
        em.close();
    }
    public void close(){
        emf.close();
    }
}
