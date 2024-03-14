package hotel.dtos;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import hotel.ressources.User;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private String username;
    private String password;
    Set<String> roles = new HashSet<>();
    
    public UserDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public UserDTO(String username, Set<String> roles) {
        this.username = username;
        this.roles = roles;
    }
    
    public UserDTO(User user) {
        this.username = user.getUsername();
        this.roles = user.getRoles().stream()
                .filter(r -> r != null && r.getRoleName().length() > 0)
                .map(r -> r.getRoleName())
                .collect(Collectors.toSet());
    }

    public void addRole(String role) {
        if(role.length() < 1) return;
        roles.add(role);
    }

    public Set<String> getRoles(){
        return roles.stream().filter(r -> r.length() > 0 && r != null).collect(Collectors.toSet());
    }
}