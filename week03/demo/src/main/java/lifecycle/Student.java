package lifecycle;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.security.auth.login.AccountException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    int id;

    @Column(name = "first_name")
    String firstName;
    @Column(name = "last_name")
    String lastName; 
    @Column(name = "email")
    String email;
    @Column(name = "age")
    int age;

    public Student(){}

    @PrePersist
    @PreUpdate
    protected void validateCreate() {
        Pattern p = Pattern.compile("^[\\w\\.]+@([\\w]+\\.)+[\\w]{2,4}");
        if (!p.matcher(email).matches()) {
            throw new RuntimeException(email); //todo make bedere exeption
        }
    }
}
