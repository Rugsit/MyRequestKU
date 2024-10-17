package ku.cs.views.components;

import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Button;
import ku.cs.services.Observer;
import ku.cs.services.Theme;

import java.util.HashMap;
import java.util.List;

public class DefaultButton extends Button implements Observer<HashMap<String,String>> {
    protected Button button;
    protected ImageView imageView;
    protected String buttonName;
    protected String baseColorHex;
    protected String hoverColorHex;
    protected String baseLabelColor;
    protected final String DEFAULT_FONT;
    protected final String FALLBACK_FONT;
    private double onLoadFontSize;
    protected boolean observeTextFont;
    protected boolean observeTextSize;
    protected boolean observeTheme;
    private Theme theme = Theme.getInstance();

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

        observeTextFont = true;
        observeTextSize = true;
        observeTheme = false;

        if(baseColorHex.equalsIgnoreCase("transparent")){
            observeTheme = true;
        }
        theme.addObserver(this);
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

        observeTextFont = true;
        observeTextSize = true;
        observeTheme = false;

        if(baseColorHex.equalsIgnoreCase("transparent")){
            observeTheme = true;
        }
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
        Font curFont = button.getFont();
        FontWeight curFontWeight = currrentFontWeight(curFont);
        double curFontSize = curFont.getSize();

        fontName = getAvailableFont(fontName);
        Font newFont = Font.font(fontName,curFontWeight,curFontSize);
        button.setFont(newFont);
        onLoadFontSize = curFontSize;
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
        onLoadFontSize = fontSize;
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
        new SquareImage(imageView,image);
    }
    public void setButtonSize(double width,double height){
        button.setPrefSize(width, height);
    }
    public Button getButton(){
        return button;
    }
    protected void updateTextSize(HashMap<String, String> data){
        if(!observeTextSize)return;
        Font curFont = button.getFont();
        FontWeight curFontWeight = currrentFontWeight(curFont);

        double tmpFontSize = onLoadFontSize;
        changeText(button.getText(),theme.getCalculatedFontSize(onLoadFontSize),curFontWeight);
        onLoadFontSize = tmpFontSize;

    }
    protected void updateTextFont(HashMap<String, String> data){
        if(!observeTextFont)return;

        double tmpFontSize = onLoadFontSize;
        setFont(data.get("textFont"));
        onLoadFontSize = tmpFontSize;
    }
    @Override
    public void update(HashMap<String, String> data) {
        updateTextSize(data);
        updateTextFont(data);

        if(!observeTheme)return;
        changeLabelColor(data.get("textColor"));
        this.hoverColorHex = data.get("primary");
    }
}
