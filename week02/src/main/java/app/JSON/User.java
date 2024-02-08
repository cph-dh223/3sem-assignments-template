package app.JSON;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User {
    private String firstName;
    private String lastName;
    private int birthDate;
    private Address address;
    private Account account;

    public User(String firstName, String lastName, int birthDate, Address address, Account account) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.address = address;
        this.account = account;
    }
    public User(){}
}
