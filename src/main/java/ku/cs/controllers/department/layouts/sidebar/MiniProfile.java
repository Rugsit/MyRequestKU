package ku.cs.controllers.department.layouts.sidebar;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import ku.cs.controllers.department.components.DefaultLabel;
import ku.cs.controllers.department.components.RouteButton;
import ku.cs.controllers.department.components.SquareImage;

public class MiniProfile {
    private final VBox vBox;
    private final ImageView profileImage;
    private final Label profileName;
    private final RouteButton logoutButton;

    private final String BASE_COLOR = "transparent";
    private final String HOVER_COLOR = "#a6a6a6";
    private final String BASE_LABEL_COLOR = DefaultLabel.DEFAULT_LABEL_COLOR;

    public MiniProfile(Image image, String name){
        this.vBox = new VBox();
        vBox.setPrefWidth(260);
        vBox.setPrefHeight(260);
        this.profileImage = new ImageView();
        profileImage.setFitWidth(100);
        profileImage.setFitHeight(100);

        this.profileName = new Label();

        new SquareImage(profileImage,image).setClipImage(1000,1000);
        new DefaultLabel(profileName).setText(name,24, FontWeight.BOLD);
        this.logoutButton = new RouteButton("login",BASE_COLOR,HOVER_COLOR,BASE_LABEL_COLOR);
        this.logoutButton.chnageText("ออกจากระบบ",18,FontWeight.NORMAL);

        vBox.getChildren().addAll(profileImage,profileName,logoutButton);
        initStyle();
    }
    private void initStyle(){
        vBox.setStyle("-fx-alignment: center; -fx-spacing: 5");
    }
    public void mount(double x, double y) {
        vBox.setLayoutX(x);
        vBox.setLayoutY(y);
    }
    public VBox getVBox() {
        return vBox;
    }
}
