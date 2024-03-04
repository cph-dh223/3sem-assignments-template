package day1_2.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class DogDTO {
    int id;
    String name;
    String breed;
    Gender gender;
    int age;

    public static enum Gender{
        MALE,
        FEMALE,
        OTHER,
    }

    public void incAge() {
        age++;
    }
}
