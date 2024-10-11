package ku.cs.views.layouts.theme;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ku.cs.views.components.BlankPopupStack;
import ku.cs.views.components.DefaultButton;
import ku.cs.views.components.DefaultComboBox;

public class themeSettingPopup extends BlankPopupStack {
    private VBox mainBox;
    private DefaultButton lightButton;
    private DefaultButton darkButton;
    private DefaultComboBox<String> textSizeSelector;
    private DefaultComboBox<String> textFontSelector;

    private void initButtons() {
        lightButton = new DefaultButton("#ABFFA4","#80BF7A","#000000"){
            @Override
            protected void handleClickEvent() {
                getButton().setOnMouseClicked(e->{
                    theme.setTheme("default");
                });
            }
        };
        lightButton.changeText("L");
        darkButton = new DefaultButton("#ABFFA4","#80BF7A","#000000"){
            @Override
            protected void handleClickEvent() {
                getButton().setOnMouseClicked(e->{
                    theme.setTheme("dark");
                });
            }
        };
        darkButton.changeText("D");
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

        textFontSelector = new DefaultComboBox<>() {
            @Override
            protected void setStringExtractor() {
                super.setStringExtractor();
            }
        };
        textFontSelector.getItems().addAll(theme.getAvailableTextFont());
        handleTextFontSelectorChange();
        textFontSelector.getSelectionModel().select(theme.getTheme().get("textFont"));
    }
    private void handleTextSizeSelectorChange(){
        textSizeSelector.valueProperty().addListener((observable, oldValue, newValue) -> {
            theme.setTextSize(newValue);
        });
    }
    private void handleTextFontSelectorChange(){
        textFontSelector.valueProperty().addListener((observable, oldValue, newValue) -> {
            theme.setTextFont(newValue);
        });
    }

    private void verifyTheme(){
        if(theme.getTheme() == null)theme.setTheme("default");
    }

    @Override
    protected void initPopupView(){
        verifyTheme();
        mainBox = new VBox();
        mainBox.setMaxWidth(300);
        mainBox.setMaxHeight(300);
        mainBox.setStyle("-fx-background-color: white;");
        initButtons();
        initComboBoxes();
        HBox line1 = new HBox();
        line1.getChildren().addAll(lightButton, darkButton,textSizeSelector);



        HBox line2 = new HBox();
        line2.getChildren().add(textFontSelector);


        HBox lineEnd = new HBox(secondButton);
        mainBox.getChildren().addAll(line1,line2,lineEnd);
        stackPane.getChildren().addAll(mainBox);
    }
}
