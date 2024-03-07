package day1_2.vet.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import day1_2.dog.main;
import day1_2.dog.dtos.DogDTO;
import day1_2.vet.dtos.Appointment;
import day1_2.vet.dtos.PatientDetails;
import io.javalin.http.Context;
import io.javalin.http.Handler;

public class PatientController {
    private static List<Appointment> appointments = new ArrayList<>();
    private static List<PatientDetails> patients = new ArrayList<>();

    public static ObjectMapper objectMapper = new ObjectMapper();

    
    public static Handler getAppointments() {
        return ctx -> {
            String json = objectMapper.writeValueAsString(appointments);
            ctx.status(200).json(json);
        };
    }
    public static Handler getAppointmentById() {
        return ctx -> {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            String json = objectMapper.writeValueAsString(appointments.stream().filter(a -> a.getId() == id).collect(Collectors.toList()).get(0));
            ctx.status(200).json(json);
        };
    }

    public static Handler getAllPatients(){
        return ctx -> {
            String json = objectMapper.writeValueAsString(patients);
            ctx.status(200).json(json);
        };
    }

    public static Handler getPatientById() {
        return ctx -> {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            String json = objectMapper.writeValueAsString(patients.stream().filter(p -> p.getId() == id).collect(Collectors.toList()).get(0));
            ctx.status(200).json(json);
        };
    }
}
