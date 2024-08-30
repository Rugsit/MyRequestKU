package ku.cs.models.user;

import ku.cs.models.user.exceptions.UserException;

import java.io.Serializable;
import java.util.ArrayList;

public class UserList implements Serializable {
    private ArrayList<User> users;
    public UserList() {
        users = new ArrayList<>();
    }

    public void addUser(String id,
                   String username,
                   String role,
                   String firstname,
                   String lastname,
                   String birthdate,
                   String email,
                   String password) throws UserException {
        User user = new User(id,username,role,firstname,lastname,birthdate,email,password);
        users.add(user);
    }
    public void addUser(String uuid,
                        String id,
                        String username,
                        String role,
                        String firstname,
                        String lastname,
                        String birthdate,
                        String email,
                        String password) throws UserException {
        User user = new User(uuid,id,username,role,firstname,lastname,birthdate,email,password);
        users.add(user);
    }
    public User findUserById(String id){
        if(id != null && !id.isEmpty()){
            for(User user : users){
                if(user.isID(id))
                    return user;
            }
        }
        return null;
    }
    public User findUserByUsername(String username){
        if(username != null && !username.isEmpty()){
            for(User user : users){
                if(user.isUsername(username))
                    return user;
            }
        }
        return null;
    }
    public ArrayList<User> getUsers(){
        return users;
    }
    @Override
    public String toString() {
        return users.toString();
    }

//    public static void main(String[] args) {
//        UserList users = new UserList();
//        try {
//            users.addUser("6610402230", "b6610402230", "student", "Sirisuk", "Tharntham", "2004-11-29", "sirisuk.t@ku.th", "123456789");
//            users.addUser("6610402078", "b6610402078", "faculty", "Tanaanan", "Chalermpan", "2004-09-26", "tanaanan.c@ku.th", "123456789");
//        } catch (Exception e){
//            System.out.println("Error : " + e);
//        }
//
//    }


}
