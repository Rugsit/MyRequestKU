package ku.cs.views.components;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import ku.cs.services.Observer;
import ku.cs.services.Theme;

import java.util.HashMap;

public class TextFieldStack extends StackPane implements Observer<HashMap<String,String>> {
    private StackPane stackBox;
    private HBox textFieldHBox;
    private DefaultTextField textField;
    private HBox buttonHBox;
    private DefaultButton resetButton;
    private DefaultButton clearButton;
    private String currentData;

    private String editColorHex;
    private String editTextColorHex;
    private String showColorHex;
    private String showTextColorHex;

    private double fieldtWidth;
    private double fieldHeight;
    private double buttonWidth = 30;
    private double buttonHeight = 30;
    private Theme theme = Theme.getInstance();

    public TextFieldStack(String data) {
        this(data,270,50);
    }
    public TextFieldStack(String data, double w, double h) {
        this.stackBox = this;
        this.currentData = data;
        this.fieldtWidth = w;
        this.fieldHeight = h;
        this.showColorHex = "transparent";
        this.showTextColorHex = "black";
        this.editColorHex = "white";
        this.editTextColorHex = "black";

        initTextField(fieldtWidth,fieldHeight);
        initButton(buttonWidth,buttonHeight);
        initStackBox(fieldtWidth,fieldHeight);
        toggleTextField(false);
        theme.addObserver(this);
    }
    private void initTextField(double w, double h){
        textField = new DefaultTextField(currentData);
        textField.setPrefSize(w,h);

        textFieldHBox = new HBox();
        textFieldHBox.setPrefSize(w,h);
        textFieldHBox.getChildren().add(textField);
    }
    private void initButton(double w, double h){
        resetButton = new DefaultButton("transparent","transparent","black"){
            @Override
            protected void handleClickEvent() {
                button.setOnMouseClicked(e -> {
                    undoText();
                });

            }
        };
        resetButton.setPrefSize(w,h);
        resetButton.changeBackgroundRadius(20);
        Image resetImageIcon = new Image(getClass().getResourceAsStream("/images/pages/department/global/components/textfield-stack/rollback-textfield-green.png"));
        resetButton.setImage(resetImageIcon,25,25);

        clearButton = new DefaultButton("transparent","transparent","black"){
            @Override
            protected void handleClickEvent() {
                button.setOnMouseClicked(e -> {
                    clearText();
                });

            }
        };
        clearButton.setPrefSize(w,h);
        clearButton.changeBackgroundRadius(20);
        Image clearImageIcon = new Image(getClass().getResourceAsStream("/images/pages/department/global/components/textfield-stack/clear-textfield-red.png"));
        clearButton.setImage(clearImageIcon,25,25);
    }
    private void initStackBox(double w, double h){
        stackBox.setPrefSize(w,h);
        stackBox.getChildren().addAll(textFieldHBox,resetButton,clearButton);
        stackBox.setAlignment(resetButton,Pos.CENTER_RIGHT);
        stackBox.setAlignment(clearButton,Pos.CENTER_RIGHT);
        StackPane.setMargin(clearButton,new Insets(0,10,0,0));
        StackPane.setMargin(resetButton,new Insets(0,40,0,0));
    }
    public void toggleTextField(boolean editMode){
        boolean editable = editMode;
        String backgroundColor = editable ? editColorHex : showColorHex;
        String textColor = editable ? editTextColorHex : showTextColorHex;

        textField.setStyle("-fx-background-color: "+backgroundColor+";"
                +"-fx-background-radius: 20;" +
                "-fx-text-fill: "+textColor+";");
        textField.setEditable(editable);

        resetButton.setVisible(editable);
        resetButton.setFocusTraversable(false);
        clearButton.setVisible(editable);
        clearButton.setFocusTraversable(false);


        currentData = textField.getText();
    }
    private void undoText(){
        textField.setText(currentData);
    }
    private void clearText(){
        textField.setText("");
    }
    public void setPlaceholder(String placeholder){
        textField.setPromptText(placeholder);
        clearText();
    }
    public String getData(){
        return currentData;
    }

    @Override
    public void update(HashMap<String, String> data) {
        textField.update(data);
        this.editTextColorHex = data.get("textColor");
        this.showTextColorHex = data.get("textColor");
        this.editColorHex = data.get("primary");
    }
}
