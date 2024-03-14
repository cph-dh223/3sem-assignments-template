package hotel.controlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import com.fasterxml.jackson.databind.ObjectMapper;
import hotel.daos.HotelDAO;
import hotel.ressources.Hotel;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;

public class HotelController implements IController{

    private Map<Integer, Hotel> hotels;
    private HotelDAO hotelDAO;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public HotelController(HotelDAO hotelDAO){
        this.hotelDAO = hotelDAO;
        hotels = new HashMap<>();
    }

    public Handler getAll() {
        return ctx -> {
            updateHotes();
            String json = objectMapper.writeValueAsString(hotels.values());
            ctx.status(HttpStatus.OK).json(json);
        };
        
    }

    @Override
    public Handler getById() {
        return ctx -> {
            if(hotels.size() == 0) {
                updateHotes();
            }
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Hotel hotel = hotels.get(id);
            if(hotel == null) {
                ctx.status(HttpStatus.BAD_REQUEST);
                return;
            }
            String json = objectMapper.writeValueAsString(hotel);
            ctx.status(HttpStatus.OK).json(json);
        };
    }

    private void updateHotes() {
            List<Hotel> hotelList = hotelDAO.getAll();
            hotels = hotelList.stream().collect(Collectors.toMap(h -> h.getId(), h -> h));    
    }

    public Handler create() {
        return ctx -> {
            if(hotels.size() == 0) {
                updateHotes();
            }
            Hotel hotel = ctx.bodyAsClass(Hotel.class);

            hotel = hotelDAO.create(hotel);

            String json = objectMapper.writeValueAsString(hotel);
            ctx.status(HttpStatus.CREATED).json(json);
        };
    }
    // /create?name=steve&id=10&
    public Handler delete() {
        return ctx -> {
            if(hotels.size() == 0) {
                updateHotes();
            }
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Hotel hotel = hotels.get(id);
            hotelDAO.delete(hotel);

            ctx.status(HttpStatus.NO_CONTENT);
        };
    }

    public Handler getHotelRooms() {
        return ctx -> {
            if(hotels.size() == 0) {
                updateHotes();
            }
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            String json = objectMapper.writeValueAsString(hotels.get(id).getRooms());
            ctx.status(HttpStatus.OK).json(json);
        };
    }

    @Override
    public Handler update() {
        return ctx -> {
            if(hotels.size() == 0) {
                updateHotes();
            }
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Hotel changedHotel = ctx.bodyAsClass(Hotel.class);
            Hotel hotel = hotels.get(id);
            hotel.setName(changedHotel.getName());
            hotel.setAddress(changedHotel.getAddress());

            hotelDAO.update(hotel);

            String json = objectMapper.writeValueAsString(hotel);
            ctx.status(HttpStatus.OK).json(json);
        };
    }


    
}
