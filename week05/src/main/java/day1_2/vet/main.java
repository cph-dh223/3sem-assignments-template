package day1_2.vet;

import java.util.Random;
import java.util.function.Supplier;
import day1_2.dog.controllers.DogController;
import day1_2.dog.controllers.KeyNotFoundExeption;
import day1_2.dog.dtos.DogDTO;
import day1_2.vet.controllers.PatientController;
import io.javalin.Javalin;
import static io.javalin.apibuilder.ApiBuilder.*;
import io.javalin.apibuilder.EndpointGroup;

public class main{
    public static void main(String[] args) {

        Javalin app = Javalin.create().start(7070);
        app.routes(getResourses());
        
        app.exception(KeyNotFoundExeption.class, (e, ctx) -> {
            ctx.status(404);
        }).error(404, ctx -> {
            ctx.result("404 not found");
        });        
    }
    


    private static EndpointGroup getResourses(){
        return  () -> {
            get("/",ctx -> ctx.result("Hello World"));
            path("/api/vet", () -> {
                path("/Appointments", () -> {
                    get(PatientController.getAppointments());
                    get("/{id}", PatientController.getAppointmentById());
                });
                path("/Patients", () -> {
                    get(PatientController.getAllPatients());
                    get("/{id}", PatientController.getPatientById());
                });
            });
        };
    }
}