package ku.cs.models.user;

import ku.cs.models.user.exceptions.UserException;

public class Advisor extends DepartmentUser{

    public Advisor(String id, String username, String role, String firstname, String lastname, String lastLogin, String email, String password, String faculty, String department) throws UserException {
        super(id, username, role, firstname, lastname, lastLogin, email, password, faculty, department);
    }

    public Advisor(String uuid, String id, String username, String role, String firstname, String lastname, String lastLogin, String email, String password, String avatar, String activeStatus, String defaultPassword, String faculty, String department) throws UserException {
        super(uuid, id, username, role, firstname, lastname, lastLogin, email, password, avatar, activeStatus, defaultPassword, faculty, department);
    }
}
