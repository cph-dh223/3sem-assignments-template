package hotel.ressources;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import hotel.dtos.RoomDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int roomNumber;
    private boolean occupied;
    private float price;

    @ManyToOne
    @ToString.Exclude
    @JsonBackReference
    private Hotel hotel;

    public Room(int roomNumber, Hotel hotel) {
        this.roomNumber = roomNumber;
        this.hotel = hotel;
        occupied = false;
    }

    public Room(RoomDTO roomDTO) {
        this.id = roomDTO.id();
        this.roomNumber = roomDTO.number();
        this.price = roomDTO.price();
        if(hotel == null){
            //TODO: some way to add hotel to this by its id, maybe send hotel map as argument
        }
        if(hotel.getId() != roomDTO.hotelId()){
            throw new RuntimeException(); //TODO
        }
    }

    public Room(int roomNumber, float price) {
        this.roomNumber = roomNumber;
        this.price = price;
    }

    
    
}
