package ku.cs.models.user;

import ku.cs.models.user.exceptions.UserException;

public class Admin extends User {
    public Admin(String id, String username, String role, String firstname, String lastname, String lastLogin, String email, String password) throws UserException {
        super(id, username, role, firstname, lastname, lastLogin, email, password);
    }

    public Admin(String uuid, String id, String username, String role, String firstname, String lastname, String lastLogin, String email, String password, String avatar, String activeStatus) throws UserException {
        super(uuid, id, username, role, firstname, lastname, lastLogin, email, password, avatar, activeStatus);
    }
}
