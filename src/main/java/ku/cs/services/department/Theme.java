package ku.cs.services.department;

import java.util.ArrayList;
import java.util.HashMap;

public class Theme implements Subject<HashMap<String,String>>{
    private static Theme instance;
    private HashMap<String,String> theme;
    private ArrayList<Observer> observers;
    private Theme() {
        observers = new ArrayList<>();
    }
    public static final Theme getInstance() {
        if(instance == null) {
            instance = new Theme();
        }
        return instance;
    }
    public void setTheme(String themeName){
        if(theme == null) {
            theme = new HashMap<>();
        }
        theme.clear();
        if(themeName.equalsIgnoreCase("dark")) {
            theme.put("name", themeName);
            theme.put("primary", "black");
            theme.put("secondary", "#1E1E1E");
            theme.put("textColor", "white");
        }else{
            theme.put("name", themeName);
            theme.put("primary", "white");
            theme.put("secondary", "#F4F4F4");
            theme.put("textColor", "black");
        }
        notifyObservers(theme);
    }

    public String getThemeName(){
        return theme.get("name");
    }
    public HashMap<String,String> getTheme(){
        return theme;
    }
    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void clearObservers(){
        observers.clear();
    }


    @Override
    public void notifyObservers(HashMap data) {
        for(Observer o : observers) {
            o.update(data);
        }

    }
    public void notifyObservers(HashMap data, Class<?>[] observerType) {
        for (Observer o : observers) {
            for(Class c : observerType) {
                if (c.isInstance(o)) {
                    o.update(data);
                }
            }

        }
    }
}
