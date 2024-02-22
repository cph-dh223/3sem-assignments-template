package Recycling.modle;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class WasteTruck {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String brand;
    private int capacity;
    @Column(name = "is_available")
    private boolean isAvailable;
    @Column(name ="registration_number")
    private String registrationNumber;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Driver> drivers;
    
    public WasteTruck(String brand, int capacity, String registrationNumber) {
        this.brand = brand;
        this.capacity = capacity;
        this.registrationNumber = registrationNumber;
        isAvailable = true;
    }

    public void addDriver(Driver driver) {
        drivers.add(driver);
    }

    public void removeDriverById(String driverId) {
        drivers.remove(
            drivers.stream()
            .parallel()
            .filter(a -> 
                a.getId().equals(driverId))
            .collect(Collectors.toList()).get(0)
        );
        
    }
    

    
}
