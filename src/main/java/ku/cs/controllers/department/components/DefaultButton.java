package ku.cs.controllers.department.components;

import javafx.fxml.FXML;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Button;

import java.util.List;

public class DefaultButton {
    @FXML protected Button button;
    protected String buttonName;
    protected String baseColorHex;
    protected String hoverColorHex;
    protected String baseLabelColor;
    protected final String DEFAULT_FONT;
    protected final String FALLBACK_FONT;

    public DefaultButton(Button button,String baseColorHex,String hoverColorHex,String baseLabelColorHex){
        this.button = button;
        this.baseColorHex = baseColorHex;
        this.hoverColorHex = hoverColorHex;
        this.buttonName = button.getId();
        this.baseLabelColor = baseLabelColorHex;
        
        this.DEFAULT_FONT = DefaultLabel.DEFAULT_FONT;
        this.FALLBACK_FONT = DefaultLabel.FALLBACK_FONT;

        setFont(DEFAULT_FONT);
        changeColor(baseColorHex);
        changeLabelColor(baseLabelColorHex);

        handleHoverEvent();
        handleClickEvent();
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
        Font curFont = button.getFont();
        FontWeight curFontWeight = currrentFontWeight(curFont);
        double curFontSize = curFont.getSize();

        fontName = getAvailableFont(fontName);
        Font newFont = Font.font(fontName,curFontWeight,curFontSize);
        button.setFont(newFont);
    }
    public void changeColor(String colorHex){
        button.setStyle(button.getStyle() + "-fx-background-color: " + colorHex + ";");
    }
    public void changeBackgroundRadius(int radius){
        button.setStyle(button.getStyle() + "-fx-background-radius: " + radius + ";");
    }
    public void changeLabelColor(String colorHex){
        button.setStyle(button.getStyle() + "-fx-text-fill: " + colorHex + ";");
    }
    private void handleHoverEvent(){
        button.setOnMouseEntered(
                (e -> changeColor(hoverColorHex))
        );
        button.setOnMouseExited(
                (e -> changeColor(baseColorHex))
        );
    }
    protected void handleClickEvent(){
        button.setOnMouseClicked(e -> {
            System.out.println(buttonName + "clicked!");
        });
    }



}
