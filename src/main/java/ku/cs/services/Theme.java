package ku.cs.services;

import javafx.scene.layout.AnchorPane;

public class Theme {
    private static Theme theme;
    private String currentTheme = "light";
    private String currentFont = "font-medium.css";

    private Theme () {}

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
}
