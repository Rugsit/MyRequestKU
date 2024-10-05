package ku.cs.views.components;

import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Button;
import ku.cs.services.Theme;

import java.util.List;

public class DefaultButton extends Button {
    protected Button button;
    protected ImageView imageView;
    protected String buttonName;
    protected String baseColorHex;
    protected String hoverColorHex;
    protected String baseLabelColor;
    protected final String DEFAULT_FONT;
    protected final String FALLBACK_FONT;

    public DefaultButton(String baseColorHex,String hoverColorHex,String baseLabelColorHex){
        this.button = this;
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
        this.button.getStyleClass().add("medium-font-size");
    }
    public DefaultButton    (Button button,String baseColorHex,String hoverColorHex,String baseLabelColorHex){
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
        this.button.getStyleClass().add("medium-font-size");
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
    protected void handleHoverEvent(){
        button.setOnMouseEntered(
                (e -> {
                    changeColor(hoverColorHex);
                    button.setCursor(Cursor.HAND);
                })
        );
        button.setOnMouseExited(
                (e -> {
                    changeColor(baseColorHex);
                    button.setCursor(Cursor.DEFAULT);
                })
        );
    }
    protected void handleClickEvent(){
        button.setOnMouseClicked(e -> {
            System.out.println(buttonName + "clicked!");
        });
    }
    public void changeText(String text){
        button.setText(text);
    }
    public void changeText(String text, double fontSize, FontWeight fontWeight){
        String fontName = getAvailableFont(DEFAULT_FONT);
        Font newFont = Font.font(fontName,fontWeight,fontSize);
        button.setFont(newFont);
        button.setText(text);
    }
    public void setImage(Image image,double width,double height){
        if(button.getGraphic() == null){
            imageView = new ImageView();
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            button.setGraphic(imageView);
        }
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
//        imageView.setImage(image);
        new SquareImage(imageView,image);
    }
    public void setButtonSize(double width,double height){
        button.setPrefSize(width, height);
    }
    public Button getButton(){
        return button;
    }

    public void changeHoverColor(String colorHex) {
        this.hoverColorHex = colorHex;
    }

    public void changeBaseColor(String baseColorHex) {
        this.baseColorHex = baseColorHex;
    }


}
