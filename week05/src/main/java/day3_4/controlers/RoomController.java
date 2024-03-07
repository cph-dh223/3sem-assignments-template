package day3_4.controlers;

import com.fasterxml.jackson.databind.ObjectMapper;

import day3_4.daos.RoomDAO;
import io.javalin.http.Handler;

public class RoomController implements IController{
    private RoomDAO roomDAO;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public RoomController(RoomDAO roomDAO){
        this.roomDAO = roomDAO;
    }

    @Override
    public Handler getAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

    @Override
    public Handler getById() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }

    @Override
    public Handler create() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public Handler delete() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public Handler update() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }
    
}
