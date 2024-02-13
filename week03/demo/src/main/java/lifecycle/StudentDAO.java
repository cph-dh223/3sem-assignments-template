package lifecycle;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

public class StudentDAO implements IStudentDAO{
    private EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig(); 
    public void createStudent(Student student){
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            // Student is in managed state (after persist)
            em.persist(student);
            // Student is in detached state after the transaction is committed
            em.getTransaction().commit();
        }
    }
    
    public Student readStudent(int id){
        EntityManager em = emf.createEntityManager();
        Student s = em.find(Student.class, id);
        em.close();
        return s;
    }
    
    public Student updateStudent(Student student){
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Student updStd = em.merge(student);
        em.getTransaction().commit();
        em.close();
        return updStd;
    }
    
    public void deleteStudent(int id){
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Student student = readStudent(id);
        if (student != null){
            em.remove(student);
        }
        em.getTransaction().commit();
        em.close();
    }
    
    public List<Student> readAllStudents(){
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        String sql = "FROM Student"; // WHY TF IS IT NOT: SELECT * FROM student?!?!?!?
        TypedQuery<Student> tq = em.createQuery(sql, Student.class);
        List<Student> students = tq.getResultList();
        em.close();
        return students;
    }
    public void close(){
        emf.close();
    }
}
