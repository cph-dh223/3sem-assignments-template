package hotel.dtos;

import java.util.Set;

import hotel.ressources.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDTO {
    private String username;
    private String password;
    private Set<String> roles;

    public UserDTO (User user){
        username = user.getUsername();
        password = user.getPassword();
        roles    = user.getRolesAsStrings();
    }

    public UserDTO(String username, Set<String> rolesSet) {
        this.username = username;
        roles = rolesSet;
    }
}
