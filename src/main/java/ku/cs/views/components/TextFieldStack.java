package ku.cs.views.components;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class TextFieldStack extends StackPane{
    private StackPane stackBox;
    private HBox textFieldHBox;
    private DefaultTextField textField;
    private HBox buttonHBox;
    private DefaultButton resetButton;
    private DefaultButton clearButton;
    private String currentData;

    private double fieldtWidth;
    private double fieldHeight;
    private double buttonWidth = 30;
    private double buttonHeight = 30;

    public TextFieldStack(String data) {
        this(data,270,50);
    }
    public TextFieldStack(String data, double w, double h) {
        this.stackBox = this;
        this.currentData = data;
        this.fieldtWidth = w;
        this.fieldHeight = h;
        initTextField(fieldtWidth,fieldHeight);
        initButton(buttonWidth,buttonHeight);
        initStackBox(fieldtWidth,fieldHeight);
        toggleTextField(false);
    }
    private void initTextField(double w, double h){
        textField = new DefaultTextField(currentData);
        textField.setPrefSize(w,h);

        textFieldHBox = new HBox();
        textFieldHBox.setPrefSize(w,h);
        textFieldHBox.getChildren().add(textField);
    }
    private void initButton(double w, double h){
        resetButton = new DefaultButton("#FFA4A4","#E19494","black"){
            @Override
            protected void handleClickEvent() {
                button.setOnMouseClicked(e -> {
                    undoText();
                });

            }
        };
        resetButton.setPrefSize(w,h);
        resetButton.changeBackgroundRadius(20);
        resetButton.setText("R");

        clearButton = new DefaultButton("#FFA4A4","#E19494","black"){
            @Override
            protected void handleClickEvent() {
                button.setOnMouseClicked(e -> {
                    clearText();
                });

            }
        };
        clearButton.setPrefSize(w,h);
        clearButton.changeBackgroundRadius(20);
        clearButton.setText("C");

//        buttonHBox = new HBox();
//        buttonHBox.setPrefSize(w,h);
//        buttonHBox.getChildren().add(button);
    }
    private void initStackBox(double w, double h){
        stackBox.setPrefSize(w,h);
        stackBox.getChildren().addAll(textFieldHBox,resetButton,clearButton);
        stackBox.setAlignment(resetButton,Pos.CENTER_RIGHT);
        stackBox.setAlignment(clearButton,Pos.CENTER_RIGHT);
        StackPane.setMargin(clearButton,new Insets(0,10,0,0));
        StackPane.setMargin(resetButton,new Insets(0,50,0,0));
    }
    public void toggleTextField(boolean editMode){
//        System.out.println(editMode);
        boolean editable = editMode;
        String backgroundColor = editable ? "white" : "transparent";

        textField.setStyle("-fx-background-color: "+backgroundColor+";"
                +"-fx-background-radius: 20");
        textField.setEditable(editable);

        resetButton.setVisible(editable);
        clearButton.setVisible(editable);


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
    }
    public String getData(){
        return currentData;
    }
}
