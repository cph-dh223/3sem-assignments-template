package day1_2.controllers;

public class KeyNotFoundExeption extends RuntimeException {

    public KeyNotFoundExeption(String msg) {
        super(msg);
    }

}
