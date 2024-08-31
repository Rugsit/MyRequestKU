package ku.cs.models.user;

import com.sun.source.tree.Tree;
import ku.cs.models.user.exceptions.UserException;

import java.io.Serializable;
import java.util.*;

public class UserList implements Serializable {
    private HashSet<User> users;
    public UserList() {
        users = new HashSet<>();
    }

    public static void main(String[] args) {
        UserList users = new UserList();
        System.out.println(users.hashCode());

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

    public boolean haveUser(User user){
        return users.contains(user);
    }
    public User findUserByObject(User user){
        if(user != null){
            if(haveUser(user)){
                for(User u : users){
                    if(u.equals(user))
                        return u;
                }
            }
        }
        return null;

    }
    public HashSet<User> getUsers(){
        return users;
    }
    @Override
    public String toString() {
        return users.toString();
    }


}
