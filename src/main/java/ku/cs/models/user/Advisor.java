package ku.cs.models.user;

import ku.cs.models.request.Request;
import ku.cs.models.user.exceptions.UserException;

import java.util.ArrayList;

public class Advisor extends DepartmentUser{

    public Advisor(String id, String username, String role, String firstname, String lastname, String lastLogin, String email, String password, String faculty, String department) throws UserException {
        super(id, username, role, firstname, lastname, lastLogin, email, password, faculty, department);
    }

    public Advisor(String uuid, String id, String username, String role, String firstname, String lastname, String lastLogin, String email, String password, String avatar, String activeStatus, String faculty, String department) throws UserException {
        super(uuid, id, username, role, firstname, lastname, lastLogin, email, password, avatar, activeStatus, faculty, department);
    }

    public UserList getAdvisesStudent(){
        return null;
    }
}
