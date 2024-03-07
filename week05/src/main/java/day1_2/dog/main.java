package day1_2.dog;

import java.util.Random;
import java.util.function.Supplier;
import day1_2.dog.controllers.DogController;
import day1_2.dog.controllers.KeyNotFoundExeption;
import day1_2.dog.dtos.DogDTO;
import io.javalin.Javalin;
import static io.javalin.apibuilder.ApiBuilder.*;
import io.javalin.apibuilder.EndpointGroup;

public class main{
    public static void main(String[] args) {

        Javalin app = Javalin.create().start(7070);
        app.routes(getDogResourses());
        
        app.exception(KeyNotFoundExeption.class, (e, ctx) -> {
            ctx.status(404);
        }).error(404, ctx -> {
            ctx.result("404 not found");
        });        
    }
    
    public static Supplier<DogDTO> dogSupplier (){
        String[] names = new String[]{"Fido","Cerberus","Spots"};
        var idSupplier = new Supplier<Integer>() {
            Integer id = 0;
            @Override 
            public Integer get(){
                return id++;
            }
        };
        return () -> {
            Random random = new Random();
            int age = random.nextInt(15);
            String name = names[random.nextInt(3)];
            DogDTO.Gender gender = DogDTO.Gender.values()[random.nextInt(3)];
            return new DogDTO(idSupplier.get(),name,"null",gender, age);
        };
    }

    private static EndpointGroup getDogResourses(){
        return  () -> {
            get("/",ctx -> ctx.result("Hello World"));
            path("/api/dogs", () -> {
                get("/dogs", ctx -> DogController.getAllDogs(ctx));
                path("/dog", () -> {
                    post(ctx -> DogController.createDog());
                    path("/{id}", () -> {
                        get(ctx -> DogController.getDogById(ctx, Integer.parseInt(ctx.pathParam("id"))));
                        put(ctx -> DogController.updateDog(ctx));
                        delete(ctx -> DogController.deleteDog(Integer.parseInt(ctx.pathParam("id"))));
                    });
                });
            });
        };
    }
}