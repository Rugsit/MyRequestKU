package ku.cs.views.layouts.theme;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import ku.cs.services.Theme;
import ku.cs.views.components.*;

import java.util.HashMap;

public class themeSettingPopup extends BlankPopupStack {
    private VBox mainBox;
    private DefaultLabel titleLabel;
    private DefaultButton switchThemeButton;
    private DefaultComboBox<String> textSizeSelector;
    private DefaultComboBox<String> textFontSelector;
    @Override
    protected void initialize(){
        super.initialize();
        theme.addObserver(this);
    }

    private void initButtons() {
        switchThemeButton = new DefaultButton("white","white","white"){
            private Image defaultImage = new Image(getClass().getResourceAsStream("/images/icons/sun-icon.png"));
            private Image darkImage = new Image(getClass().getResourceAsStream("/images/icons/moon-icon.png"));
            {
                String curTheme = theme.getCurrentTheme();
                Image iconImage;
                if(curTheme.equals("dark")){
                    iconImage = darkImage;
                    baseColorHex = "#2731B7";
                    hoverColorHex = "#212A9E";
                    changeColor(baseColorHex);
                }else{
                    iconImage = defaultImage;
                    baseColorHex = "#69EEFF";
                    hoverColorHex = "#62DCEC";
                    changeColor(baseColorHex);
                }
                setImage(iconImage,100,100);
                setButtonSize(150,150);
                changeBackgroundRadius(150);
            }
            @Override
            protected void handleClickEvent() {
                getButton().setOnMouseClicked(e->{
                    String curTheme = theme.getCurrentTheme();
                    Image iconImage;
                    if(curTheme.equals("dark")){
                        Theme.getInstance().setCurrentTheme("light");
                        theme.setTheme("default");
                        iconImage = defaultImage;
                        baseColorHex = "#69EEFF";
                        hoverColorHex = "#62DCEC";
                        changeColor(baseColorHex);
                    }else{
                        theme.setTheme("dark");
                        Theme.getInstance().setCurrentTheme("dark");
                        iconImage = darkImage;
                        baseColorHex = "#2731B7";
                        hoverColorHex = "#212A9E";
                        changeColor(baseColorHex);
                    }
                    setImage(iconImage,100,100);
                });
            }
        };
        switchThemeButton.changeText("");
    }


    private void initComboBoxes(){
        textSizeSelector = new DefaultComboBox<>() {
            @Override
            protected void setStringExtractor() {
                super.setStringExtractor();
            }
        };
        textSizeSelector.getItems().addAll(theme.getAvailableTextSize());
        handleTextSizeSelectorChange();
        textSizeSelector.getSelectionModel().select(theme.getTheme().get("textSize"));
        textSizeSelector.changeBackgroundRadius(10);

        textFontSelector = new DefaultComboBox<>() {
            @Override
            protected void setStringExtractor() {
                super.setStringExtractor();
            }
        };
        textFontSelector.getItems().addAll(theme.getAvailableTextFont());
        handleTextFontSelectorChange();
        textFontSelector.getSelectionModel().select(theme.getTheme().get("textFont"));
        textFontSelector.changeBackgroundRadius(10);
    }
    private void handleTextSizeSelectorChange(){
        textSizeSelector.valueProperty().addListener((observable, oldValue, newValue) -> {
            theme.setTextSize(newValue);
            String pathToFile = "";
            if (newValue.equals("small")) {
                pathToFile = "font-small.css";
            } else if (newValue.equals("normal")) {
                pathToFile = "font-medium.css";
            } else if (newValue.equals("large")) {
                pathToFile = "font-large.css";
            }
            Theme.getInstance().setCurrentFont(pathToFile);
        });
    }
    private void handleTextFontSelectorChange(){
        textFontSelector.valueProperty().addListener((observable, oldValue, newValue) -> {
            theme.setTextFont(newValue);
            String pathToFile = "";
            if (newValue.equalsIgnoreCase("Krub")) {
                pathToFile = "Krub.css";
            } else if (newValue.equalsIgnoreCase("PrintAble4U")) {
                pathToFile = "printAble4u-font.css";
            }
            Theme.getInstance().setCurrentFontFamily(pathToFile);
        });
    }

    private void verifyTheme(){
        if(theme.getTheme() == null)theme.setTheme("default");
    }

    @Override
    protected void initPopupView(){
        verifyTheme();
        mainBox = new VBox();
        mainBox.setMaxWidth(400);
        mainBox.setMaxHeight(400);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setSpacing(20);
        initButtons();
        initComboBoxes();
        HBox titleLine = new HBox();
        titleLine.setAlignment(Pos.CENTER);
        titleLabel = new DefaultLabel("");
        titleLabel.changeText("เปลี่ยนธีม",32, FontWeight.BOLD);
        titleLine.getChildren().add(titleLabel);

        HBox line1 = new HBox();
        line1.setAlignment(Pos.CENTER);
        line1.getChildren().addAll(switchThemeButton);


        HBox line2 = new HBox();
        line2.setAlignment(Pos.CENTER);
        line2.setSpacing(10);
        line2.getChildren().addAll(textFontSelector,textSizeSelector);


        HBox lineEnd = new HBox(secondButton);
        lineEnd.setAlignment(Pos.CENTER);
        mainBox.getChildren().addAll(titleLine,line1,line2,lineEnd);
        stackPane.getChildren().addAll(mainBox);
        VBox.setMargin(titleLine, new Insets(20,0,0,0));
        update(theme.getTheme());
    }
    @Override
    public void update(HashMap<String, String> data) {
        super.update(data);
        mainBox.setStyle("-fx-background-color: "+data.get("secondary")+"; -fx-background-radius: 50;");
        titleLabel.update(data);
        switchThemeButton.update(data);
        textSizeSelector.update(data);
        textFontSelector.update(data);

    }
}
