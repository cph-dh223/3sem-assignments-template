package hotel.ressources;

import java.util.Set;

public interface ISecurityUser {
    Set<String>  getRolesAsStrings();
    boolean verifyPassword(String otherPassword);
    void addRole(Role role);
    void removeRole(String roleName);
}