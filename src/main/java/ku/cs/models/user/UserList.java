package ku.cs.models.user;

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
                   String lastLogin,
                   String email,
                   String password,
                   String faculty,
                   String department) throws UserException {
        User user;
        switch (role){
            case "admin":
                user = new Admin(id, username, role, firstname, lastname, lastLogin, email, password);
                break;
            case "advisor":
                user = new Advisor(id, username, role, firstname, lastname, lastLogin, email, password, faculty, department);
                break;
            case "student":
                user = new Student(id, username, role, firstname, lastname, lastLogin, email, password, faculty, department,"no-advisor");
                break;
            default:
                if(role.contains("faculty")){
                    user = new FacultyUser(id, username, role, firstname, lastname, lastLogin, email, password, faculty);
                }else if(role.contains("department")){
                    user = new DepartmentUser(id, username, role, firstname, lastname, lastLogin, email, password, faculty,department);
                }else{
                    throw new UserException("Invalid role");
                }
        }
        users.add(user);
    }
    public void addUser(String uuid,
                        String id,
                        String username,
                        String role,
                        String firstname,
                        String lastname,
                        String lastLogin,
                        String email,
                        String password,
                        String avatar,
                        String activeStatus,
                        String faculty,
                        String department,
                        String advisorUUID) throws UserException {
        User user;
        switch (role){
            case "admin":
                user = new Admin(uuid, id, username, role, firstname, lastname, lastLogin, email, password, avatar, activeStatus);
                break;
            case "advisor":
                user = new Advisor(uuid, id, username, role, firstname, lastname, lastLogin, email, password, avatar, activeStatus, faculty, department);
                break;
            case "student":
                user = new Student(uuid, id, username, role, firstname, lastname, lastLogin, email, password, avatar, activeStatus, faculty, department, advisorUUID);
                break;
            default:
                if(role.contains("faculty")){
                    user = new FacultyUser(uuid, id, username, role, firstname, lastname, lastLogin, email, password, avatar, activeStatus, faculty);
                }else if(role.contains("department")){
                    user = new DepartmentUser(uuid, id, username, role, firstname, lastname, lastLogin, email, password, avatar, activeStatus, faculty, department);
                }else{
                    throw new UserException("Invalid role");
                }
        }
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


    public UserList getFacultyList() {
        UserList list = new UserList();
        for (User user : users) {
            if (user instanceof FacultyUser){
               list.addUser(user);
            }
        }
        return list;
    }

    public UserList getDepartmentList() {
        UserList list = new UserList();
        for (User user : users) {
            if (user instanceof DepartmentUser){
                list.addUser(user);
            }
        }
        return list;
    }

    public UserList getAdvisorList() {
        UserList list = new UserList();
        for (User user : users) {
            if (user instanceof Advisor){
                list.addUser(user);
            }
        }
        return list;
    }

    public UserList getStudentList() {
        UserList list = new UserList();
        for (User user : users) {
            if (user instanceof Student){
                list.addUser(user);
            }
        }
        return list;
    }
}
