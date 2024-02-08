package app.JSON;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDTO {
    String fullName;
    String city;
    String zipCode;
    String isActive;
    
    public UserDTO(String fullName, String city, String zipCode, String isActive) {
        this.fullName = fullName;
        this.city = city;
        this.zipCode = zipCode;
        this.isActive = isActive;
    }
    
    public static UserDTO userToDTO(User user){
        return new UserDTO(user.getFirstName() + " " + user.getLastName(), user.getAddress().getCity(), user.getAddress().getZipCode() + "", user.getAccount().isActive + "");
    }
    public static UserDTO[] usersToDTOs(User[] users){
        UserDTO[] userDTOs = new UserDTO[users.length];
        for (int i = 0; i < userDTOs.length; i++) {
            userDTOs[i] = userToDTO(users[i]);
        }
        return userDTOs;
    }
}
