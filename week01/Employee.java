import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;

public class Employee {

    public String name;
    public int age;
    public LocalDate birthDay;

    public Employee(String name, int age) {
        
        this.name = name;
        this.age = age;
    }
    
    public Employee(String name, LocalDate birhtDay){
        this.name = name;
        this.birthDay = birhtDay;
        age = Period.between(birthDay, LocalDate.now()).getYears();
    }
}
