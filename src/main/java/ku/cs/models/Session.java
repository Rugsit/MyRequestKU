package ku.cs.models;

import ku.cs.models.user.User;

import java.util.HashMap;

public class Session {
    private HashMap<String,Object> map;
    public Session() {
        map = new HashMap<>();
        map.put("user",null);
        map.put("data",null);
    }
    public User getUser() {
        return (User)map.get("user");
    }
    public void setUser(User user) {
        map.put("user",user);
    }
    public void clear(){
        map.put("user",null);
    }
    public Object getData() {
        return map.get("data");
    }
    public void setData(Object data) {
        map.put("data",data);
    }
}






