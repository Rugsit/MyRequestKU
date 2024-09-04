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

    public void addUser(String id,
                   String username,
                   String role,
                   String firstname,
                   String lastname,
                   String birthdate,
                   String email,
                   String faculty,
                   String department,
                   String password) throws UserException {
        User user = new User(id,username,role,firstname,lastname,birthdate,email, faculty, department,password);
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
                        String faculty,
                        String department,
                        String password,
                        String avatar) throws UserException {
        User user = new User(uuid,id,username,role,firstname,lastname,birthdate,email,faculty,department,password,avatar);
        users.add(user);
    }
    public User findUserById(String id){
        if(id != null && !id.isEmpty()){
            for(User user : users){
                if(user.isId(id))
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

    public void deleteUserByObject(User user){
        if(user != null && haveUser(user)){
            try{
                users.remove(user);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    public HashSet<User> getUsers(){
        return users;
    }
    @Override
    public String toString() {
        return users.toString();
    }
}
