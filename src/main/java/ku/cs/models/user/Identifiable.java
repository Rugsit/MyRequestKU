package ku.cs.models.user;

import ku.cs.models.user.exceptions.PasswordException;

import java.util.Date;
import java.util.UUID;

public interface Identifiable {
    //Identical data getter
    UUID getUUID();
    String getID();
    String getUsername();
    String getRole();

    String getName();
    String getFirstname();
    String getLastname();
    Date getLastLogin();
    String getEmail();
    String getFaculty();
    String getDepartment();
    //Identify method
    Boolean isUUID(UUID uuid);
    Boolean isID(String id);
    Boolean isUsername(String username);
    Boolean isRole(String role);
    Boolean validatePassword(String password) throws PasswordException;


}
