package ku.cs.models.user;

import ku.cs.models.user.exceptions.UserException;

import java.io.Serializable;
import java.util.*;

public class UserList implements Serializable {
    private HashMap<String, ArrayList<User>> users;
    public UserList() {
        users = new HashMap<>();
        for(String role : Identifiable.AVAILABLE_ROLES){
            users.put(role, new ArrayList<>());
        }
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
        users.get(role).add(user);
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
        users.get(role).add(user);
    }
    public void addUser(User user) throws UserException {
        if(user != null){
            if(!haveUser(user)){
                users.get(user.getRole()).add(user);
            }
        }
    }
    public User findUserById(String queryId){
        if(queryId != null && !queryId.isEmpty()){
            for(ArrayList<User> list : users.values()){
                Collections.sort(list,User.userIdComparator);
                int low = 0;
                int high = list.size()-1;
                while(low <= high){
                    int mid = low + (high - low) / 2;
                    if(list.get(mid).getId().compareTo(queryId) == 0){
                        return list.get(mid);
                    }
                    // If query greater, ignore left half
                    if (list.get(mid).getId().compareTo(queryId) < 0)
                        low = mid + 1;

                    // If query is smaller, ignore right half
                    else
                        high = mid - 1;
                }
            }
        }
        return null;
    }
    public User findUserByUsername(String queryUsername){
        if(queryUsername != null && !queryUsername.isEmpty()){
            for(ArrayList<User> list : users.values()){
                Collections.sort(list,User.usernameComparator);
                int low = 0;
                int high = list.size()-1;
                while(low <= high){
                    int mid = low + (high - low) / 2;
                    if(list.get(mid).getUsername().compareTo(queryUsername) == 0){
                        return list.get(mid);
                    }
                    // If query greater, ignore left half
                    if (list.get(mid).getUsername().compareTo(queryUsername) < 0)
                        low = mid + 1;

                        // If query is smaller, ignore right half
                    else
                        high = mid - 1;
                }
            }
        }
        return null;
    }

    public boolean haveUser(User user){
        boolean found = false;
        for(ArrayList<User> list : users.values()){
            if(list.contains(user)){
                found = true;
            }
        }
        return found;
    }
    public User findUserByObject(User queryUser){
        if(queryUser != null){
            for(ArrayList<User> list : users.values()){
                Collections.sort(list);
                int low = 0;
                int high = list.size()-1;
                while(low <= high){
                    int mid = low + (high - low) / 2;
                    if(list.get(mid).compareTo(queryUser) == 0){
                        return list.get(mid);
                    }
                    // If query greater, ignore left half
                    if (list.get(mid).compareTo(queryUser) < 0)
                        low = mid + 1;

                    // If query is smaller, ignore right half
                    else
                        high = mid - 1;
                }
            }
        }
        return null;

    }

    public void deleteUserByObject(User user){
        if(user != null && haveUser(user)){
            try{
                users.get(user.getRole()).remove(user);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    public Collection<User> getUsers(){
        ArrayList<User> list = new ArrayList<>();
        for(ArrayList<User> roleList : users.values()){
            list.addAll(roleList);
        }
        return list;
    }
    public Collection<User> getUsers(String role){
        if(!Identifiable.AVAILABLE_ROLES.contains(role))return  null;
        return users.get(role);
    }
    public void concatenate(UserList userList){
        for(String role : users.keySet()){
            users.get(role).addAll(userList.users.get(role));
        }
    }
    @Override
    public String toString() {
        return users.toString();
    }
}
