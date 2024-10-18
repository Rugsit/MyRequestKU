package ku.cs.views.components;

import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.FontWeight;
import ku.cs.services.Observer;
import ku.cs.services.Theme;

import java.util.HashMap;

public class BlankPopupStack extends StackPane implements Observer<HashMap<String,String>>{
    protected StackPane stackPane;
    protected Pane backgroundPane;
    protected DefaultButton firstButton;
    protected DefaultButton secondButton;
    protected Theme theme = Theme.getInstance();
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
        firstButton = new DefaultButton("#78D88C","#5AA469","white");
        firstButton.setButtonSize(215,60);
        firstButton.changeBackgroundRadius(25);
        firstButton.changeText("ยืนยัน",28,FontWeight.NORMAL);
        secondButton = new DefaultButton("#FF8080","#BC5F5F","white");
        secondButton.setButtonSize(215,60);
        secondButton.changeBackgroundRadius(25);
        secondButton.changeText("ยกเลิก",28,FontWeight.NORMAL);
        handleFirstButton();
        handleSecondButton();
    }
    public DefaultButton getFirstButton(){
        return firstButton;
    }
    public DefaultButton getSecondButton(){
        return secondButton;
    }
    public StackPane getStackPane(){return stackPane;}
    protected void initPopupView(){

    };
    protected void handleFirstButton(){
        firstButton.setOnMouseClicked(e -> {
        });
    }
    protected void handleSecondButton(){
        secondButton.setOnMouseClicked(e ->{
        });
    }
    @Override
    public void update(HashMap<String, String> data) {
        firstButton.update(data);
        secondButton.update(data);
    }
}
