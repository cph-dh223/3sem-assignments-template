package hotel.daos;

import hotel.exceptions.EntityNotFoundException;
import hotel.ressources.Role;
import hotel.ressources.User;

public interface ISecurityDAO {
    User createUser(String username, String password);
    Role createRole(String role);
    User addRoleToUser(String username, String role);
    User verifyUser(String username, String password) throws EntityNotFoundException;
}
