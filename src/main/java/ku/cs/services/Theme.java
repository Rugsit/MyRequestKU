package ku.cs.services;

import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.HashMap;

public class Theme implements Subject<HashMap<String,String>>{
    private static Theme instance;
    private static Theme theme;
    private HashMap<String,String> themeList;
    private ArrayList<Observer> observers;
    private String currentTheme = "light";
    private String currentFont = "font-medium.css";
    private String currentFontFamily = "printAble4u-font.css";

    private Theme () {
        observers = new ArrayList<>();
    }

    public static final Theme getInstance() {
        if (theme == null) {
            theme = new Theme();
        }
        return theme;
    }


    public void loadCssToPage(AnchorPane anchorPane, PathGenerator path) {
        anchorPane.getStylesheets().clear();
        if (currentTheme.equals("dark")) {
            anchorPane.getStylesheets().add(path.getThemeDarkPath());
        } else if (currentTheme.equals("light")) {
            anchorPane.getStylesheets().add(path.getThemeLightPath());
        }
        anchorPane.getStylesheets().add(Theme.class.getResource("/ku/cs/styles/font/" + currentFont).toString());
    }

    public String getCurrentTheme() {
        return currentTheme;
    }

    public void setCurrentTheme(String currentTheme) {
        this.currentTheme = currentTheme;
    }

    public String getCurrentFont() {
        return currentFont;
    }

    public void setCurrentFont(String currentFont) {
        this.currentFont = currentFont;
    }

    public String getCurrentFontFamily() {
        return currentFontFamily;
    }

    public void setCurrentFontFamily(String currentFontFamily) {
        this.currentFontFamily = currentFontFamily;
    }
    public void setTheme(String themeName){
        if(themeList == null) {
            themeList = new HashMap<>();
        }
        themeList.clear();
        if(themeName.equalsIgnoreCase("dark")) {
            themeList.put("name", themeName);
            themeList.put("primary", "black");
            themeList.put("secondary", "#1E1E1E");
            themeList.put("textColor", "white");
        }else{
            themeList.put("name", themeName);
            themeList.put("primary", "white");
            themeList.put("secondary", "#F4F4F4");
            themeList.put("textColor", "black");
        }
        notifyObservers(themeList);
    }

    public String getThemeName(){
        return themeList.get("name");
    }
    public HashMap<String,String> getTheme(){
        return themeList;
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
