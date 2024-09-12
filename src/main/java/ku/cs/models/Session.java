package ku.cs.models;

import ku.cs.models.user.User;

import java.util.HashMap;

public class Session {
    private User user;
    private HashMap<String,Object> data;
    public Session() {
        this.user = null;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        if(user != null) this.user = user;
    }
    public void clear(){
        this.user = null;
    }
    public HashMap<String,Object> getData() {
        return data;
    }
    public void setData(HashMap<String,Object> data) {
        this.data = data;
    }
}






