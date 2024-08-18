package ku.cs.controllers.department.layouts.sidebar;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import ku.cs.controllers.department.components.RouteButton;

public class NavList {
    private VBox vBox;
    private double fontSize = 24;
    private FontWeight fontWeight = FontWeight.NORMAL;
    private double vboxWidth;
    private double vboxHeight;
    private double navButtonWidth;
    private double navButtonHeight = 50;

    public NavList(double width, double height) {
        this.vboxWidth = width;
        this.vboxHeight = height;
        this.navButtonWidth = width * 0.85;
        initVBox(this.vboxWidth,this.vboxHeight);
    }
    private void initVBox(double width, double height) {
        vBox = new VBox();
        vBox.setPrefSize(width, height);
    }
    private void setVBoxStyle(){
        vBox.setStyle("-fx-alignment: CENTER;");
        double marginTop = 0;
        int nodeSize = vBox.getChildren().size();
        double space =  (vboxHeight - ((nodeSize * navButtonHeight) + marginTop))/(nodeSize-1) * 0.5;
        for(int i = 0;i < vBox.getChildren().size();i++){
            Node node  = vBox.getChildren().get(i);
            if(node instanceof Button && i == 0){
                VBox.setMargin(node, new Insets(marginTop,0,0,0));
            }else{
                VBox.setMargin(node, new Insets(space,0,0,0));
            }
        }
    }
    public void addRouteButton(
            String buttonText,
            String routeName,
            String baseColorHex,
            String hoverColorHex,
            String baseLabelColorHex,
            String iconImagePath

    ){
        RouteButton newButton = new RouteButton(
                routeName,
                baseColorHex,
                hoverColorHex,
                baseLabelColorHex
        );
        newButton.changeText(buttonText,fontSize,fontWeight);
        newButton.setButtonSize(navButtonWidth,navButtonHeight);
        newButton.changeBackgroundRadius(15);

        Image iconImage = new Image(getClass().getResourceAsStream(iconImagePath));
        newButton.setImage(iconImage,40,40);

        newButton.setStyle(newButton.getStyle() + "-fx-alignment: BASELINE-LEFT;");

        vBox.getChildren().add(newButton.getButton());

    }
    public void setMount(double x,double y){
        setVBoxStyle();
        vBox.setLayoutX(x);
        vBox.setLayoutY(y);
    }
    public VBox getVBox() {
        return vBox;
    }


}
