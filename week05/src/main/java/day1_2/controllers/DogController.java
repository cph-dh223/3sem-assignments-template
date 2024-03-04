package day1_2.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import day1_2.main;
import day1_2.dtos.DogDTO;
import io.javalin.http.Context;

public class DogController {
    public static Map<Integer,DogDTO> dogMap = Stream.generate(main.dogSupplier()).limit(100).collect(Collectors.toMap(a -> a.getId(), a -> a));

    public static void getAllDogs(Context ctx){
        ObjectMapper objectMapper = new ObjectMapper();
        
        try {
            objectMapper.writeValue(ctx.outputStream(), dogMap.values());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void getDogById(Context ctx, int id){
        if(!dogMap.containsKey(id)){
            throw new KeyNotFoundExeption("Dog not found");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(ctx.outputStream(), dogMap.get(id));
        } catch (IOException e) {
            ctx.status(404);
        }

    }
    public static void createDog(){
        DogDTO newDog = main.dogSupplier().get();
        dogMap.put(newDog.getId(), newDog);
    }
    public static void updateDog(Context ctx){
        dogMap.get(Integer.parseInt(ctx.pathParam("id"))).incAge();
    }
    public static void deleteDog(int id){
        dogMap.remove(dogMap.get(id));
    }
}
