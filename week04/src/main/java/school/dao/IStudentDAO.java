package school.dao;

import java.util.List;

import school.modle.Semester;
import school.modle.Student;
import school.modle.Teacher;

public interface IStudentDAO {
    // find all students in the system with the first name Anders
    List<Student> findAllStudentsByFirstName(String firstName);

    // find all students in the system with the last name And
    List<Student> findAllStudentsByLastName(String lastName);

    // find the total number of students, for a semester given the semester name as a parameter
    long findTotalNumberOfStudentsBySemester(String semesterName);

    // find the total number of students that has a particular teacher
    long findTotalNumberOfStudentsByTeacher(Teacher teacher);

    // find the teacher who teaches the most semesters
    Teacher findTeacherWithMostSemesters();

    // find the semester that has the fewest students
    Semester findSemesterWithFewestStudents();

    // find all students, encapsulated as StudentInfo
    StudentInfo getAllStudentInfo(int id);
}