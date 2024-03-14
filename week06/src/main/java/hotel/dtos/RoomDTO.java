package hotel.dtos;

public record RoomDTO(
    int id, 
    int hotelId, 
    int number,
    float price
) {    
}
