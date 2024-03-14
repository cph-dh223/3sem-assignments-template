package hotel.dtos;

import java.util.List;

import hotel.ressources.Room;

public record HotelDTO(
    int id, 
    String name, 
    String address, 
    List<Room> rooms
) {
} 
