package ku.cs.views.components;

import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class DefaultLabel {
    protected Label label;
    public static final String DEFAULT_FONT = "PrintAble4U";
    public static final String FALLBACK_FONT = "Arial";
    public static final String DEFAULT_LABEL_COLOR = "#000000";

    public DefaultLabel(Label label) {
        this.label = label;
        setFont(DEFAULT_FONT);
        changeLabelColor(DEFAULT_LABEL_COLOR);
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
    public void setText(String text){
        label.setText(text);
    }
    public void setText(String text,double fontSize,FontWeight fontWeight){
        String fontName = getAvailableFont(DEFAULT_FONT);
        Font newFont = Font.font(fontName,fontWeight,fontSize);
        label.setFont(newFont);
        label.setText(text);
    }
}

