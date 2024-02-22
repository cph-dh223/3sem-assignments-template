package dolphin;

import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DolphinDAO {
    private static DolphinDAO instanse;
    public static EntityManagerFactory emf;
    public static DolphinDAO getDolphinDAOInstanse(EntityManagerFactory _emf){
        if (instanse == null) {
            emf = _emf;
            instanse = new DolphinDAO();
        }
        return instanse;
    }
    public static void close() {
        emf.close();
    }
    public Person save(Person p) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(p);
            em.getTransaction().commit();
        } 
        return p;
    }

    public Person getById(int id){
        try(EntityManager em = emf.createEntityManager()){
            return em.find(Person.class, id);
        }
    }

    public int getTotalPaid(int id){
        try (EntityManager em = emf.createEntityManager()){
            String sql = "SELECT f FROM Person p JOIN p.fees f";
            
            TypedQuery<Fee> frees =  em.createQuery(sql, Fee.class);
            
            return frees.getResultStream().filter(f -> f.getPerson().getId() == id).map(a -> a.getAmount()).reduce((a,b) -> a + b).get().intValue();
        }

    }
    public List<Note> getAllPersonsNotes(int id){
        try (EntityManager em = emf.createEntityManager()){
            String sql = "SELECT n FROM Person p JOIN p.notes n";
            
            TypedQuery<Note> frees =  em.createQuery(sql, Note.class);
            
            return frees.getResultStream().filter(f -> f.getPerson().getId() == id).toList();
        }

    }

}
