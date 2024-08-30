package ku.cs.controllers;

import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.models.user.exceptions.UserException;
import javax.naming.AuthenticationException;

public class AuthenticationController {
    private UserList users = new UserList();
    public AuthenticationController() {
        this.users = hardCodeDatasource();
    }


    public UserList hardCodeDatasource() {
        try {
            users.addUser("6610402230", "b6610402230", "student", "Sirisuk", "Tharntham", "2004-11-29", "sirisuk.t@ku.th", "123456789");
            users.addUser("6610402078", "b6610402078", "faculty", "Tanaanan", "Chalermpan", "2004-09-26", "tanaanan.c@ku.th", "123456789");
        } catch (UserException e) {
            System.out.println("Error adding user : " + e.getMessage());
        }
        return users;
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

//    public static void main(String[] args) {
//        AuthenticationController authController = new AuthenticationController();
//
//        try {
//            UserList users = authController.hardCodeDatasource();
//            System.out.println("Users in the list: " + users.getUsers());
//
//            // Test with valid credentials (ensure hashed passwords are used for actual testing)
//            User userCheck = authController.loginAuthenticate("b6610402078", "123456789"); // Use the correct hashed password here
//            if (userCheck != null) {
//                System.out.println("Authenticated User: " + userCheck);
//                System.out.println("Role : " + userCheck.getRole());
//            } else {
//                System.out.println("User not found.");
//            }
//        } catch (AuthenticationException e) {
//            System.out.println("Authentication failed: " + e.getMessage());
//        }
//    }
}

