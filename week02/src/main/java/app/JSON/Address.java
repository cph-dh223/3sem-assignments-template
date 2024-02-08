package app.JSON;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Address {
    private String street;
    private String city;
    private int zipCode;

    public Address(){}
    public Address(String street, String city, int zipCode) {
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
    }
    
}
