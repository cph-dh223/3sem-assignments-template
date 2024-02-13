package lifecycle;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

public class Main {
    private static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig();
    public static void main(String[] args) {
        // Student is in transient state
        Student s = new Student();
        s.setAge(1);
        s.setEmail("email@email.dk");
        s.setFirstName("firstName");
        s.setLastName("lastName");
        createStudent(s);
        readAllStudents().forEach(System.out::println);;
        
        emf.close();
        
        IStudentDAO sdao = new StudentDAO();
        
        Student s2 = new Student();
        s.setAge(2);
        s.setEmail("email2@email.dk");
        s.setFirstName("firstName2");
        s.setLastName("lastName2");
        sdao.close();
    }
    public static void createStudent(Student student){
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            // Student is in managed state (after persist)
            em.persist(student);
            // Student is in detached state after the transaction is committed
            em.getTransaction().commit();
        }
    }
    
    public static Student readStudent(int id){
        EntityManager em = emf.createEntityManager();
        Student s = em.find(Student.class, id);
        em.close();
        return s;
    }
    
    public static Student updateStudent(Student student){
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Student updStd = em.merge(student);
        em.getTransaction().commit();
        em.close();
        return updStd;
    }
    
    public static void deleteStudent(int id){
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Student student = readStudent(id);
        if (student != null){
            em.remove(student);
        }
        em.getTransaction().commit();
        em.close();
    }
    
    public static List<Student> readAllStudents(){
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        String sql = "FROM Student"; // WHY TF IS IT NOT: SELECT * FROM student?!?!?!?
        TypedQuery<Student> tq = em.createQuery(sql, Student.class);
        List<Student> students = tq.getResultList();
        em.close();
        return students;
    }
}
