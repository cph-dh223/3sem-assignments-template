package school;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import school.config.HibernateConfig;
import school.dao.StudentDAO;
import school.modle.Semester;
import school.modle.Student;
import school.modle.Teacher;

public class Populate {
    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig("dolphin", false);

        insert(emf);
        StudentDAO sdao = new StudentDAO(emf);

        // System.out.println(sdao.findAllStudentsByFirstName("a"));
        // System.out.println(sdao.findAllStudentsByLastName("a"));
        // System.out.println(sdao.findTotalNumberOfStudentsBySemester("dat 1"));
        // System.out.println(sdao.findTotalNumberOfStudentsByTeacher(new Teacher("t1", "t1")));
        // System.out.println(sdao.findTeacherWithMostSemesters());
        // System.out.println(sdao.findSemesterWithFewestStudents());
        System.out.println(sdao.getAllStudentInfo(1));
    }

    private static void insert(EntityManagerFactory emf) {
        Semester s1 = new Semester("dat 1", "dat 1");
        Semester s2 = new Semester("dat 2", "dat 2");
        Semester s3 = new Semester("dat 3", "dat 3");

        Teacher t1 = new Teacher("t1", "t1");
        Teacher t2 = new Teacher("t2", "t2");
        Teacher t3 = new Teacher("t3", "t3");

        s1.addTeacher(t1);
        s1.addTeacher(t2);
        s1.addTeacher(t3);
        t1.addSemester(s1);
        t2.addSemester(s1);
        t3.addSemester(s1);
        
        s2.addTeacher(t2);
        s2.addTeacher(t3);
        t2.addSemester(s2);
        t3.addSemester(s2);

        s3.addTeacher(t3);
        t3.addSemester(s3);

        Student st1 = new Student("a", "a");
        Student st2 = new Student("b", "b");
        Student st3 = new Student("c", "c");
        Student st4 = new Student("d", "d");
        Student st5 = new Student("e", "e");
        Student st6 = new Student("f", "f");

        s1.addStudent(st1);
        s1.addStudent(st2);

        s2.addStudent(st3);
        s2.addStudent(st4);

        s3.addStudent(st5);
        s3.addStudent(st6);

        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(s1);
            em.persist(s2);
            em.persist(s3);

            em.persist(t1);
            em.persist(t2);
            em.persist(t3);
            
            em.persist(st1);
            em.persist(st2);
            em.persist(st3);
            em.persist(st4);
            em.persist(st5);
            em.persist(st6);
            em.getTransaction().commit();
        }
    }
}
