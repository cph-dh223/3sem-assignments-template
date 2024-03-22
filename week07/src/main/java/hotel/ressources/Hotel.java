package hotel.ressources;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import hotel.dtos.HotelDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String address;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "hotel", cascade = CascadeType.MERGE)
    @JsonManagedReference
    private List<Room> rooms;
    public Hotel(){
        rooms = new ArrayList<>();
    }
    
    public Hotel(HotelDTO hotelDTO){
        this.id = hotelDTO.id();
        this.name = hotelDTO.name();
        this.address = hotelDTO.address();
        this.rooms = hotelDTO.rooms();
        rooms.forEach(r -> r.setHotel(this));
    }

    public Hotel(String name, String address) {
        this.name = name;
        this.address = address;
        rooms = new ArrayList<>();
    }

    public void addRoom(Room room){
        rooms.add(room);
        room.setHotel(this);
    }
}
