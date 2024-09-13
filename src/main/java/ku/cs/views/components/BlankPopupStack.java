package ku.cs.views.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;

public class BlankPopupStack extends StackPane {
    protected StackPane stackPane;
    protected Pane backgroundPane;
    protected DefaultButton acceptButton;
    protected DefaultButton declineButton;
    public BlankPopupStack() {
        initButton();
        stackPane = this;
        backgroundPane = new Pane();
        initialize();
    }
    protected void initialize(){
        initStackPain();
        initBackgroundPane();
        stackPane.getChildren().add(backgroundPane);
        initPopupView();
    }
    private void initStackPain(){
        stackPane.setLayoutX(0);
        stackPane.setLayoutY(0);
        stackPane.setPrefSize(1280, 720);
        stackPane.setStyle("-fx-background-color: transparent;");
        stackPane.setAlignment(Pos.CENTER);
    }
    private void initBackgroundPane() {
        backgroundPane.setLayoutX(0);
        backgroundPane.setLayoutY(0);
        backgroundPane.setPrefSize(1280, 720);
        backgroundPane.setStyle("-fx-background-color: black");
        backgroundPane.setOpacity(0.3);
    }
    protected void initButton(){
        acceptButton = new DefaultButton("#78D88C","#5AA469","white");
        acceptButton.setButtonSize(215,60);
        acceptButton.changeBackgroundRadius(25);
        acceptButton.changeText("ยืนยัน",28,FontWeight.NORMAL);
        declineButton = new DefaultButton("#FF8080","#BC5F5F","white");
        declineButton.setButtonSize(215,60);
        declineButton.changeBackgroundRadius(25);
        declineButton.changeText("ยกเลิก",28,FontWeight.NORMAL);
        handleAcceptButton();
        handleDeclineButton();
    }
    public DefaultButton getAcceptButton(){
        return acceptButton;
    }
    public DefaultButton getDeclineButton(){
        return declineButton;
    }
    public StackPane getStackPane(){return stackPane;}
    protected void initPopupView(){

    };
    protected void handleAcceptButton(){
        acceptButton.setOnMouseClicked(e -> {
            System.out.println("Accept button clicked");
        });
    }
    protected void handleDeclineButton(){
        declineButton.setOnMouseClicked(e ->{
            System.out.println("Decline button clicked");
        });
    }
}
