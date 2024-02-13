package lifecycle;

import java.util.List;

public interface IStudentDAO {
    public void createStudent(Student student);
    public Student readStudent(int id);
    public Student updateStudent(Student student);
    public void deleteStudent(int id);
    public List<Student> readAllStudents();
    public void close();

}
