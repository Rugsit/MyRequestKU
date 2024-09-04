package ku.cs.models.user;

import ku.cs.models.user.exceptions.PasswordException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

public interface Identifiable {
    //public static final
    ArrayList<String> AVAILABLE_ROLES = new ArrayList<>(Arrays.asList(new String[]{
            "admin",
            "advisor",
            "faculty-staff",
            "faculty-approver",
            "department-staff",
            "department-approver",
            "student"
    }));
    //Identical data getter
    UUID getUUID();
    String getId();
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
    Boolean isId(String id);
    Boolean isUsername(String username);
    Boolean isRole(String role);
    Boolean validatePassword(String password) throws PasswordException;


}
