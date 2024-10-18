package ku.cs.models.user;

import ku.cs.models.user.exceptions.PasswordException;

import java.time.LocalDateTime;
import java.util.UUID;

public interface Identifiable {
    //Identical data getter
    UUID getUUID();
    String getId();
    String getUsername();
    String getRole();

    String getName();
    String getFirstname();
    String getLastname();
    LocalDateTime getLastLogin();
    String getEmail();

    //Identify method
    Boolean isUUID(UUID uuid);
    Boolean isId(String id);
    Boolean isUsername(String username);
    Boolean isRole(String role);
    Boolean validatePassword(String password) throws PasswordException;


}
