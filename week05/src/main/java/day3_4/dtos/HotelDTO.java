package day3_4.dtos;

import java.util.List;

import day3_4.ressources.Room;

public record HotelDTO(int id, String name, String address, List<Room> rooms) {
} 
