package hotel.ressources;


import jakarta.persistence.*;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * Purpose: To handle security in the API
 *  Author: Thomas Hartmann
 */
@Entity
@Table(name = "roles")
@ToString
public class Role implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "name", length = 20)
    private String name;

    @ManyToMany(mappedBy = "roles")
    @ToString.Exclude
    @JsonBackReference
    private Set<User> users = new HashSet<>();

    public Role() {}

    public Role(String roleName) {
        this.name = roleName;
    }

    public String getRoleName() {
        return name;
    }
    public Set<User> getUsers() {
        return users;
    }

    public void addUser(User user){
        users.add(user);
    }

}
