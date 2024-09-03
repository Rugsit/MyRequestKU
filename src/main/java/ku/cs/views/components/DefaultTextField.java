package ku.cs.views.components;

import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class DefaultTextField extends TextField {
    protected final String DEFAULT_FONT;
    protected final String FALLBACK_FONT;
    protected double fontSize = 24;
    public DefaultTextField() {this("");}
    public DefaultTextField(String data) {
        super(data);
        this.DEFAULT_FONT = DefaultLabel.DEFAULT_FONT;
        this.FALLBACK_FONT = DefaultLabel.FALLBACK_FONT;

        this.setFont(DEFAULT_FONT);
        this.setFontSize(fontSize);
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
        Font curFont = this.getFont();
        FontWeight curFontWeight = currrentFontWeight(curFont);
        double curFontSize = curFont.getSize();

        fontName = getAvailableFont(fontName);
        Font newFont = Font.font(fontName,curFontWeight,curFontSize);
        this.setFont(newFont);
    }
    public void setFontSize(double size){
        Font curFont = this.getFont();
        String curFontName = curFont.getName();

        FontWeight curFontWeight = currrentFontWeight(curFont);
        Font newFont = Font.font(curFontName,curFontWeight,size);
        this.setFont(newFont);
    }
}
