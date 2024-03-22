package hotel.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import hotel.config.HibernateConfig;
import hotel.exceptions.EntityNotFoundException;
import hotel.ressources.Hotel;
import hotel.ressources.Role;
import hotel.ressources.User;

import java.util.Collection;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Purpose:
 *
 * @author: Thomas Hartmann
 */
public class UserDAO implements ISecurityDAO{

    private EntityManagerFactory emf;
    public UserDAO(EntityManagerFactory _emf) {
        this.emf = _emf;
    }

    @Override
    public User createUser(String username, String password) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        User user = new User(username, password);
        Role userRole = em.find(Role.class, "user");
        if (userRole == null){
            userRole = new Role("user");
            em.persist(userRole);
        }
        user.addRole(userRole);
        em.persist(user);
        em.getTransaction().commit();
        em.close();
        return user;
    }

    @Override
    public User verifyUser(String username, String password) throws EntityNotFoundException {
        EntityManager em = emf.createEntityManager();
        User user = em.find(User.class, username);
        if (user == null)
            throw new EntityNotFoundException("No user found with username: " + username);
        if (!user.verifyUser(password))
            throw new EntityNotFoundException("Wrong password");
        return user;
    }

    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        UserDAO dao = new UserDAO(emf);
        User user = dao.createUser("David", "1234");
        User admin = dao.createUser("Admin", "admin");
        dao.addRoleToUser("Admin", "admin");
        System.out.println(user.getUsername());
        System.out.println(admin.getUsername());
        try {
            User verifyedUser = dao.verifyUser("David", "1234");
            User verifyedAdmin = dao.verifyUser("Admin", "admin");
            System.out.println(verifyedUser.getUsername());
            System.out.println(verifyedAdmin.getUsername());
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Role createRole(String role) {
        //return null;

        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public User addRoleToUser(String username, String role) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        User user = em.find(User.class, username);
        Role userRole = em.find(Role.class, role);
        if (userRole == null){
            userRole = new Role(role);
            em.persist(userRole);
        }
        user.addRole(userRole);
        em.persist(user);
        em.getTransaction().commit();
        em.close();
        return user;
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
