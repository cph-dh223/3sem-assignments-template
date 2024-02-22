package dolphin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class DolphinDAOTest {
    
    public static EntityManagerFactory emfTest = HibernateConfig.getEntityManagerFactoryConfig("dolphin", true);
    public static DolphinDAO dao;
    
    @BeforeAll
    public static void setupAll(){
        emfTest = HibernateConfig.getEntityManagerFactoryConfig("dolphin", true);
        dao = DolphinDAO.getDolphinDAOInstanse(emfTest);
    }
    
    
    @AfterAll
    public static void afterAll(){
        DolphinDAO.close();
    }

    @BeforeEach
    public void setup(){
        dao = DolphinDAO.getDolphinDAOInstanse(emfTest);
        Person p1 = new Person("John");
        Person p2 = new Person("Doe");
        Person p3 = new Person("Jane");
        Person p4 = new Person("Smith");
        
        try (EntityManager em = DolphinDAO.emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE pperson_id_seq RESTART WITH 1").executeUpdate();
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
            em.persist(p4);
            em.getTransaction().commit();
        } 
    }
    
    @Test
    public void createTest(){
        Person p = new Person("Alice");
        dao = DolphinDAO.getDolphinDAOInstanse(emfTest);
        
        p = dao.save(p);

        assertEquals(1, (int)p.getId());
        
    }

    @Test
    public void allFeesTest(){
        dao = DolphinDAO.getDolphinDAOInstanse(emfTest);
        Person p1 = new Person("Alice");
        Person p2 = new Person("Bob");
        p2.addFee(new Fee(10000, LocalDate.now()));
        p1.addFee(new Fee(100, LocalDate.now()));
        p1.addFee(new Fee(200, LocalDate.now()));
        p1.addFee(new Fee(300, LocalDate.now()));
        p1 = dao.save(p1);
        dao.save(p2);

        int actual = dao.getTotalPaid(p1.getId());

        assertEquals(600, actual);
    }
    @Test
    public void allNotesTest(){
        dao = DolphinDAO.getDolphinDAOInstanse(emfTest);
        Person p1 = new Person("Alice");
        Person p2 = new Person("Bob");
        p2.addNote(new Note("hellow", LocalDate.now()));
        Note n1 = new Note("1", LocalDate.now());
        Note n2 = new Note("2", LocalDate.now());
        Note n3 = new Note("3", LocalDate.now());
        p1.addNote(n1);
        p1.addNote(n2);
        p1.addNote(n3);
        p1 = dao.save(p1);
        dao.save(p2);

        List<Note> actual = dao.getAllPersonsNotes(p1.getId());
        p1.getNotes().forEach(a -> assertTrue(actual.contains(a)));
    }
}
