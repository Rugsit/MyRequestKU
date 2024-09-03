package ku.cs.controllers;

import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.models.user.exceptions.UserException;
import ku.cs.services.UserListFileDatasource;

import javax.naming.AuthenticationException;
import javax.swing.plaf.IconUIResource;

public class AuthenticationController {
    private UserList users;

    public AuthenticationController() {
        UserListFileDatasource datasource = new UserListFileDatasource("data", "student.csv");
        this.users = datasource.readData();
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

    public User getUserInDatasource(String username){
        for (User existUser : users.getUsers()){
            if(existUser.getUsername().equalsIgnoreCase(username)){
                return existUser;
            }
        }
        return null;
    }

    public boolean isUserInDatasource(String username){
        for (User existUser : users.getUsers()){
            if(existUser.getUsername().equalsIgnoreCase(username)){
                return true;
            }
        }
        return false;
    }

//    public static void main(String[] args) {
//        AuthenticationController authController = new AuthenticationController();
//        boolean isUserFound = authController.isUserInDatasource("b7610402078");
//        System.out.println(isUserFound);
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
    }
//}

