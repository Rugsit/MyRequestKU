package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ku.cs.controllers.department.components.*;

public class AboutUsController {
    @FXML private ImageView tanaananImageView;
    @FXML private ImageView rugsitImageView;
    @FXML private ImageView sirisukImageView;
    @FXML private ImageView narakornImageView;
    @FXML private Button backButton;

    @FXML
    private void initialize() {
        showImage();
        RouteButton back = new RouteButton(backButton, "login", "transparent",
                               "#EBEBEB", "#FFFFFF");
    }

    private void showImage() {
        Image tanaanan = new Image(getClass().getResourceAsStream("/images/team-members/tanaanan.png"));
        Image rugsit = new Image(getClass().getResourceAsStream("/images/team-members/rugsit.png"));
        Image sirisuk = new Image(getClass().getResourceAsStream("/images/team-members/sirisuk.png"));
        Image narakorn = new Image(getClass().getResourceAsStream("/images/team-members/narakorn.png"));
        CircleImage tanaananImage = new CircleImage(tanaananImageView, tanaanan);
        CircleImage rugsitImage = new CircleImage(rugsitImageView, rugsit);
        CircleImage sirisukImage = new CircleImage(sirisukImageView, sirisuk);
        CircleImage narakornImage = new CircleImage(narakornImageView, narakorn);
    }
}


