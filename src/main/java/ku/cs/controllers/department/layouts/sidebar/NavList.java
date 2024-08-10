package ku.cs.controllers.department.layouts.sidebar;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import ku.cs.controllers.department.components.DefaultLabel;
import ku.cs.controllers.department.components.RouteButton;

public class NavList {
    private VBox vBox;
    private double fontSize = 24;
    private FontWeight fontWeight = FontWeight.NORMAL;
    private  double navButtonWidth = 220;
    private double navButtonHeight = 50;

    public NavList(double width, double height) {
        initVBox(width,height);
        setVBoxStyle();
    }
    private void initVBox(double width, double height) {
        vBox = new VBox();
        vBox.setPrefSize(width, height);
    }
    private void setVBoxStyle(){
        vBox.setStyle("-fx-alignment: CENTER; -fx-spacing: 30");
    }
    public void addRouteButton(
            String buttonText,
            String routeName,
            String baseColorHex,
            String hoverColorHex,
            String baseLabelColorHex,
            String iconImagePath

    ){
        Button newButton = new Button();
        RouteButton routeButton = new RouteButton(newButton,
                routeName,
                baseColorHex,
                hoverColorHex,
                baseLabelColorHex
        );
        routeButton.setText(buttonText,fontSize,fontWeight);
        routeButton.setButtonSize(navButtonWidth,navButtonHeight);
        routeButton.changeBackgroundRadius(15);

        Image iconImage = new Image(getClass().getResourceAsStream(iconImagePath));
        routeButton.setImage(iconImage,40,40);

        newButton.setStyle(newButton.getStyle() + "-fx-alignment: BASELINE-LEFT;");

        vBox.getChildren().add(routeButton.getButton());

    }
    public void setMount(double x,double y){
        vBox.setLayoutX(x);
        vBox.setLayoutY(y);
    }
    public VBox getVBox() {
        return vBox;
    }


}
