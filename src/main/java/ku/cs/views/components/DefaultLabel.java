package ku.cs.views.components;

import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import ku.cs.services.department.Observer;
import ku.cs.services.department.Theme;

import java.util.HashMap;
import java.util.List;

public class DefaultLabel extends Label implements Observer<HashMap<String,String>> {
    protected Label label;
    public static final String DEFAULT_FONT = "PrintAble4U";
    public static final String FALLBACK_FONT = "Arial";
    public static final String DEFAULT_LABEL_COLOR = "#000000";
    private Theme theme = Theme.getInstance();
    public DefaultLabel(String text){
        super(text);
        this.label = this;
        setFont(DEFAULT_FONT);
        changeLabelColor(DEFAULT_LABEL_COLOR);
        theme.addObserver(this);
    }

    public DefaultLabel(Label label) {
        this.label = label;
        setFont(DEFAULT_FONT);
        changeLabelColor(DEFAULT_LABEL_COLOR);
        theme.addObserver(this);
    }

    protected FontWeight currrentFontWeight(Font currentFont){
        String style = currentFont.getStyle();
        if (style.contains("Bold")) {
            return FontWeight.BOLD;
        } else {
            return FontWeight.NORMAL;
        }
    }
    protected String getAvailableFont(String fontName){
        List<String> availableFonts = Font.getFamilies();
        if(availableFonts.contains(fontName)){
            return fontName;
        }else{
            return FALLBACK_FONT;
        }
    }
    public void setFont(String fontName){
        Font curFont = label.getFont();
        FontWeight curFontWeight = currrentFontWeight(curFont);
        double curFontSize = curFont.getSize();

        fontName = getAvailableFont(fontName);
        Font newFont = Font.font(fontName,curFontWeight,curFontSize);
        label.setFont(newFont);
    }
    public void changeLabelColor(String colorHex){
        label.setStyle(label.getStyle() + "-fx-text-fill: " + colorHex + ";");
    }
    public void changeText(String text){
        label.setText(text);
    }
    public void changeText(String text, double fontSize, FontWeight fontWeight){
        String fontName = getAvailableFont(DEFAULT_FONT);
        Font newFont = Font.font(fontName,fontWeight,fontSize);
        label.setFont(newFont);
        label.setText(text);
    }
    @Override
    public void update(HashMap<String, String> data) {
        changeLabelColor(data.get("textColor"));
    }
}

