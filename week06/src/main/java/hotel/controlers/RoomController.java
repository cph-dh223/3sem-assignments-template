package hotel.controlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import hotel.daos.RoomDAO;
import hotel.ressources.Room;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;

public class RoomController implements IController{
    private Map<Integer, Room> rooms;
    private RoomDAO roomDAO;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RoomController(RoomDAO roomDAO){
        this.roomDAO = roomDAO;
        rooms = new HashMap<>();
    }

    @Override
    public Handler getAll() {
        return ctx -> {
            updateRooms();
            String json = objectMapper.writeValueAsString(rooms.values());
            ctx.status(HttpStatus.OK).json(json);
        };
    }

    @Override
    public Handler getById() {
        return ctx -> {
            if(rooms.size() == 0) {
                updateRooms();
            }
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Room hotel = rooms.get(id);
            if(hotel == null) {
                ctx.status(HttpStatus.BAD_REQUEST);
                return;
            }
            String json = objectMapper.writeValueAsString(hotel);
            ctx.status(HttpStatus.OK).json(json);       
        };
    }
    @Override
    public Handler create() {
        return ctx -> {
            if(rooms.size() == 0) {
                updateRooms();
            }
            Room room = ctx.bodyAsClass(Room.class);

            room = roomDAO.create(room);

            String json = objectMapper.writeValueAsString(room);
            ctx.status(HttpStatus.CREATED).json(json);
        };
    }
    
    @Override
    public Handler delete() {
        return ctx -> {
            if(rooms.size() == 0) {
                updateRooms();
            }
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Room room = rooms.get(id);
            roomDAO.delete(room);

            ctx.status(HttpStatus.NO_CONTENT);
        };
    }
    
    @Override
    public Handler update() {
        return ctx -> {
            if(rooms.size() == 0) {
                updateRooms();
            }
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Room changedRoom = ctx.bodyAsClass(Room.class);
            Room room = rooms.get(id);
            room.setRoomNumber(changedRoom.getRoomNumber());
            room.setPrice(changedRoom.getPrice());
            
            room = roomDAO.update(room);

            String json = objectMapper.writeValueAsString(room);
            ctx.status(HttpStatus.OK).json(json);
        };
    }

    private void updateRooms(){
        List<Room> roomList = roomDAO.getAll();
        rooms = roomList.stream().collect(Collectors.toMap(r -> r.getId(), r -> r));
    }
    
}
