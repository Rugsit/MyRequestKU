package ku.cs.services;

import javafx.scene.layout.AnchorPane;
import ku.cs.views.components.DefaultLabel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Theme implements Subject<HashMap<String,String>>{
    private static Theme theme;
    private HashMap<String,String> themeList;
    private ArrayList<Observer> observers;
    private String currentTheme = "light";
    private String currentFont = "font-medium.css";
    private String currentFontFamily = "printAble4u-font.css";
    private String currentTextSize = "normal";
    private String currentTextFont = DefaultLabel.DEFAULT_FONT;
    private String[] availableTextSize = {"small","normal","large"};
    private String[] availableTextFont = {"PrintAble4U","Krub"};

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
        try {
            String css = modifyCSS("/ku/cs/styles/font/" + currentFont);
            String tempCss = "data:text/css," + css;
            anchorPane.getStylesheets().add(tempCss);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        anchorPane.getStylesheets().add(Theme.class.getResource("/ku/cs/styles/font/" + currentFontFamily).toString());
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
            themeList.put("textSize", currentTextSize);
            themeList.put("textFont", currentTextFont);
            currentTheme = "dark";
        }else{
            themeList.put("name", themeName);
            themeList.put("primary", "white");
            themeList.put("secondary", "#F4F4F4");
            themeList.put("textColor", "black");
            themeList.put("textSize", currentTextSize);
            themeList.put("textFont", currentTextFont);
            currentTheme = "light";
        }
        notifyObservers(themeList);
    }
    public void setTextSize(String textSize){
        currentTextSize = textSize;
        setTheme(currentTheme);
    }

    public String[] getAvailableTextSize(){
        return availableTextSize;
    }

    public void setTextFont(String fontName){
        currentTextFont = fontName;
        setTheme(currentTheme);
    }

    public String[] getAvailableTextFont(){
        return availableTextFont;
    }

    public double getCalculatedFontSize(double loadFontSize){
        String textSize = getTheme().get("textSize");
        if(currentTextFont.equalsIgnoreCase("PrintAble4U")){
            loadFontSize *= 0.9;
        }else{
            loadFontSize *= 0.65;
        }
        double size;
        if(textSize.equalsIgnoreCase("large")){
            size = loadFontSize*1.1;
        }else if(textSize.equalsIgnoreCase("small")){
            size = loadFontSize*0.9;
        }else{
            size = loadFontSize;
        }
        return size;
    }

    private double getCalculatedFontSizePrivate(double loadFontSize){
        double size;
        if(!currentFontFamily.contains("printAble4u")) {
            loadFontSize *= 0.7;
            if (currentFont.contains("large")) {
                size = loadFontSize * 1.05;
            } else if (currentFont.contains("small")) {
                size = loadFontSize * 0.95;
            } else {
                size = loadFontSize;
            }
        } else {size = loadFontSize;}

        return size;
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

    private String modifyCSS(String path) throws IOException {
        InputStream stream = getClass().getResourceAsStream(path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String content = "";
        String line;

        //Regex find -fx-font-size: xx;
        Pattern pattern = Pattern.compile("-fx-font-size:\\s*(\\d+(\\.\\d+)?)\\s*;");
        while ((line = reader.readLine()) != null) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                double curSize = Double.parseDouble(matcher.group(1));
                double newSize = getCalculatedFontSizePrivate(curSize);
                line = line.replaceFirst("-fx-font-size:\\s*\\d+(\\.\\d+)?;", String.format("-fx-font-size: %.2f;", newSize));
            }
            content += line + "\n";
        }

        reader.close();
        return content;
    }
}
