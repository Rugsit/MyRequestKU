package ku.cs.services;

import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.models.user.exceptions.UserException;

import javax.naming.AuthenticationException;

public class Authentication {
    private UserList users;

    public Authentication() {
        UserListFileDatasource datasource = new UserListFileDatasource("data", "student.csv");
        this.users = datasource.readAllUser();
    }


    public User loginAuthenticate(String username, String password) throws AuthenticationException {
        for (User existUser : users.getUsers()) {
            if (existUser.getUsername().equals(username)) {
                try {
                    if (existUser.validatePassword(password)) {
                        return existUser;
                    } else {
                        throw new AuthenticationException("Invalid password");
                    }
                } catch (Exception e) {
                    throw new AuthenticationException("Error validating password.");
                }
            }
        }
        return null;
    }


}

