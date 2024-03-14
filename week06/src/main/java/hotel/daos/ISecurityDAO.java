package hotel.daos;

import hotel.exceptions.ValidationException;
import hotel.ressources.Role;
import hotel.ressources.User;

public interface ISecurityDAO {
    User getVerifiedUser(String username, String password) throws ValidationException;
    User createUser(String username, String password);
    Role createRole(String roleName);
    User addUserRole(String userName, String roleName);
}
