package hotel.daos;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import hotel.exceptions.ValidationException;
import hotel.ressources.Hotel;
import hotel.ressources.Role;
import hotel.ressources.User;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;

public class UserDAO implements ISecurityDAO {

    private EntityManagerFactory emf;

    public UserDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public User getVerifiedUser(String userName, String password) throws ValidationException {
        try(var em = emf.createEntityManager()){
            User user = em.find(User.class, userName);
            if(user == null) {
                throw new EntityNotFoundException("No user found with username: " + userName);
            }
            if (!user.verifyPassword(password)) {
                throw new ValidationException("Wrong password");
            }
            return user;
        }
        
    }

    @Override
    public User createUser(String username, String password) {
        try(var em = emf.createEntityManager()){
            User newUser = em.find(User.class, username);
            if(newUser != null){
                throw new EntityExistsException("User with username: " + username + " already exists");
            }

            newUser = new User(username, password);
            Role userRole = em.find(Role.class, "user");
            em.getTransaction().begin();
            if (userRole == null){
                userRole = new Role("user");
                em.persist(userRole);
            }
            newUser.addRole(userRole);
            em.persist(newUser);
            em.getTransaction().commit();
            return newUser;
        }
    }
    
    @Override
    public Role createRole(String roleName) {
        try(var em = emf.createEntityManager()){
            Role role = new Role(roleName);
            em.getTransaction().begin();
            em.persist(role);
            em.getTransaction().commit();
            return role;
        }
        
    }

    @Override
    public User addUserRole(String userName, String roleName) {
        try(var em = emf.createEntityManager()){
            User user = em.find(User.class, userName);
            Role role = em.find(Role.class, roleName);
            if(user == null) {
                throw new EntityNotFoundException("No user found with username: " + userName);
            }
            if(role == null) {
                throw new EntityNotFoundException("No role found with rolename: " + userName);
            }
            user.addRole(role);

            em.getTransaction().begin();
            em.merge(user);
            em.merge(role);
            em.getTransaction().commit();
            return user;
        }
    }

    public List<User> getAllUsers() {
        try(var em = emf.createEntityManager()){
            TypedQuery<User> q = em.createQuery("SELECT u FROM User u", User.class);
            List<User> users = q.getResultList();            
            return users;
        }   
    }
    public List<Role> getAllRoles() {
        try(var em = emf.createEntityManager()){
            TypedQuery<Role> q = em.createQuery("SELECT r FROM Role r", Role.class);
            List<Role> roles = q.getResultList();            
            return roles;
        }   
    }

        
}
