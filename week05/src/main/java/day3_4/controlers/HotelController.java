package day3_4.controlers;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import day3_4.daos.HotelDAO;
import day3_4.ressources.Hotel;
import day3_4.ressources.Room;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import jakarta.servlet.http.HttpServletResponse;

public class HotelController implements IController{

    private Map<Integer, Hotel> hotels;
    private HotelDAO hotelDAO;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public HotelController(HotelDAO hotelDAO){
        this.hotelDAO = hotelDAO;
        hotels = new HashMap<>();
    }

    public Handler getAll() {
        List<Hotel> hotelList = hotelDAO.getAll();
        hotels = hotelList.stream().collect(Collectors.toMap(h -> h.getId(), h -> h));
        return ctx -> {
            String json = objectMapper.writeValueAsString(hotelList);
            System.out.println(json);
            ctx.status(HttpStatus.OK).json(json);
        };
        
    }

    @Override
    public Handler getById() {
        return ctx -> {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Hotel hotel = hotels.get(id);
            if(hotel == null) {
                ctx.status(HttpStatus.BAD_REQUEST);
                return;
            }
            ctx.status(HttpStatus.OK).json(hotel);
        };
    }

    public Handler addRoom() {
        return ctx -> {
            Room room = ctx.bodyAsClass(Room.class);
            
            
        };
    }

    public Handler create() {
        return ctx -> {
            Hotel hotel = ctx.bodyAsClass(Hotel.class);

            hotel = hotelDAO.create(hotel);

            String json = objectMapper.writeValueAsString(hotel);
            ctx.status(HttpStatus.ACCEPTED).json(json);
        };
    }

    public Handler delete() {
        return ctx -> {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Hotel hotel = hotels.get(id);
            hotelDAO.delete(hotel);

            String json =objectMapper.writeValueAsString(hotel);
            ctx.status(HttpStatus.ACCEPTED).json(json);

        };
    }

    public Handler getHotelRooms() {
        return ctx -> {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            String json = objectMapper.writeValueAsString(hotels.get(id).getRooms());
            ctx.status(HttpStatus.OK).json(json);
        };
    }

    @Override
    public Handler update() {
        return ctx -> {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Hotel changedHotel = ctx.bodyAsClass(Hotel.class);
            Hotel hotel = hotels.get(id);
            hotel.setName(changedHotel.getName());
            hotel.setAddress(changedHotel.getAddress());

            hotelDAO.update(hotel);

            String json =objectMapper.writeValueAsString(hotel);
            ctx.status(HttpStatus.ACCEPTED).json(json);
        };
    }


    
}
