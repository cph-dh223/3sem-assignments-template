package Recycling.modle;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Driver {
    @Id
    private String id;
    @Temporal(TemporalType.DATE)
    @Column(name = "employment_date")
    private LocalDate employmentDate;
    private String name;
    private String surname;
    private BigDecimal salary;

    @ManyToOne
    private WasteTruck truck;

    public Driver(String name, String surname, BigDecimal salary) {
        this.name = name;
        this.surname = surname;
        this.salary = salary;
    }

    private void genreateID(){
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        int dayOfMonth = employmentDate.getDayOfMonth();
        int monthOfYear = employmentDate.getMonthValue();
        sb.append(dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth);
        sb.append(monthOfYear < 10 ? "0" + monthOfYear : monthOfYear);
        sb.append(employmentDate.getYear()-2000);
        sb.append('-');
        sb.append(name.substring(0,1));
        sb.append(surname.substring(0,1));
        sb.append('-');
        sb.append(r.nextInt(100,999));
        sb.append(surname.substring(surname.length()-1, surname.length()).toUpperCase());
        id = sb.toString();
    }

    public static Boolean validateDriverId(String driverId) {
        return driverId.matches("[0-9][0-9][0-9][0-9][0-9][0-9]-[A-Z][A-Z]-[0-9][0-9][0-9][A-Z]");
    }

    @PrePersist
    public void prePersist(){
        employmentDate = LocalDate.now();
        genreateID();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Driver{id=");
        sb.append(id);
        sb.append(", name=");
        sb.append(name);
        sb.append(", surname=");
        sb.append(surname);
        sb.append("}");
        return sb.toString();
    }
}
