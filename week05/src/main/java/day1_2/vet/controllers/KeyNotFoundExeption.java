package day1_2.vet.controllers;

public class KeyNotFoundExeption extends RuntimeException {

    public KeyNotFoundExeption(String msg) {
        super(msg);
    }

}
