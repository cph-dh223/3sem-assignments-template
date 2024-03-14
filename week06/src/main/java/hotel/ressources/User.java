package hotel.ressources;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.mindrot.jbcrypt.BCrypt;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "users")
public class User implements ISecurityUser {

    
    @Id
    @Basic(optional = false)
    @Column(name = "username", length = 25)
    private String username;

    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    
    @JoinTable(name = "user_roles", joinColumns = {
            @JoinColumn(name = "user_name", referencedColumnName = "username")}, inverseJoinColumns = {
            @JoinColumn(name = "role_name", referencedColumnName = "name")})
    @JsonManagedReference
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();

    public User(String username, String password) {
        this.username = username;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public Set<String> getRolesAsStrings() {
        return roles.stream().map(r -> r.getRoleName()).collect(Collectors.toSet());
    }

    @Override
    public boolean verifyPassword(String otherPassword) {
        return BCrypt.checkpw(otherPassword, password);

    }

    @Override
    public void addRole(Role role) {
        roles.add(role);
        role.addUser(this);
    }

    @Override
    public void removeRole(String roleName) {
        roles.stream()
            .filter(r -> r.getRoleName().equals(roleName)).findFirst()
            .ifPresent(r -> {
                roles.remove(r); 
                r.getUsers().remove(this);
            }); 
    }

    

}
