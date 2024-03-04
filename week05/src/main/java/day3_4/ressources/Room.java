package day3_4.ressources;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@ToString
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private final int roomNumber;
    private boolean occupied;

    @ManyToOne
    @ToString.Exclude
    private Hotel hotel;

    public Room(int roomNumber, Hotel hotel) {
        this.roomNumber = roomNumber;
        this.hotel = hotel;
        occupied = false;
    }
    
}
