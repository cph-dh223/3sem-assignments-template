package school.dao;

import java.util.List;

import school.dao.absDAO;
import Recycling.modle.WasteTruck;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Parameter;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import school.modle.Semester;
import school.modle.Student;
import school.modle.Teacher;

public class StudentDAO extends absDAO implements IStudentDAO {

    public StudentDAO(EntityManagerFactory emf) {
        super(emf);
    }

    @Override
    public List<Student> findAllStudentsByFirstName(String firstName) {
        try(EntityManager em = emf.createEntityManager()){
            String sql = String.format("FROM Student s WHERE s.firstName = \'%s\'", firstName);
            TypedQuery<Student> q = em.createQuery(sql, Student.class);
            return q.getResultList();
        }  
    }
    
    @Override
    public List<Student> findAllStudentsByLastName(String lastName) {
        try(EntityManager em = emf.createEntityManager()){
            String sql = String.format("FROM Student s WHERE s.lastName = \'%s\'", lastName);
            TypedQuery<Student> q = em.createQuery(sql, Student.class);
            return q.getResultList();
        }  
    }
    
    @Override
    public long findTotalNumberOfStudentsBySemester(String semesterName) {
        try(EntityManager em = emf.createEntityManager()){
            String sql = String.format("SELECT COUNT(s) FROM Student s WHERE s.semester.name = \'%s\'", semesterName);
            TypedQuery<Long> q = em.createQuery(sql, Long.class);
            return q.getSingleResult();
        }  
        
    }
    
    @Override
    public long findTotalNumberOfStudentsByTeacher(Teacher teacher) {
        try(EntityManager em = emf.createEntityManager()){
            String sql = String.format("SELECT COUNT(st) FROM Student st JOIN st.semester s JOIN s.teachers t WHERE t.firstName = \'%s\' GROUP BY t.id", teacher.getFirstName());
            TypedQuery<Long> q = em.createQuery(sql, Long.class);
            return q.getSingleResult();
            // return q.getSingleResult();
        }  
        
    }
    
    @Override
    public Teacher findTeacherWithMostSemesters() {
        try(EntityManager em = emf.createEntityManager()){
            String sql = String.format("From Teacher");
            TypedQuery<Teacher> q = em.createQuery(sql, Teacher.class);
            return q.getResultStream().reduce((a,b) -> a = a.getSemesters().size() > b.getSemesters().size() ? a : b).get();
        }  
    }
    
    @Override
    public Semester findSemesterWithFewestStudents() {
        try(EntityManager em = emf.createEntityManager()){
            String sql = String.format("From Semester");
            TypedQuery<Semester> q = em.createQuery(sql, Semester.class);
            return q.getResultStream().reduce((a,b) -> a = a.getStudents().size() > b.getStudents().size() ? a : b).get();
        }
    }

    @Override
    public StudentInfo getAllStudentInfo(int id) {
        try(EntityManager em = emf.createEntityManager()){
            String sql = String.format("SELECT s.firstName, s.lastName, s.id, se.name, se.description FROM Student s JOIN s.semester se WHERE s.id = %d", id);
            Query q = em.createQuery(sql);
            Object[] o = (Object[])q.getResultList().get(0);
            String fisrtName = (String)o[0];
            String lastName  = (String)o[1];
            int studentId    = Integer.valueOf((Integer)o[2]);
            String thisSemesterName = (String)o[3];
            String thisSemesterDescription = (String)o[4];
             
            
            StudentInfo sinfo = new StudentInfo(fisrtName + " " + lastName, studentId, thisSemesterName, thisSemesterDescription);
            return sinfo;
        }  
    }
    
}
