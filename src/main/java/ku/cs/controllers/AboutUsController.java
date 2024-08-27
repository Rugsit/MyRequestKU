package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ku.cs.controllers.department.components.DefaultImage;
import ku.cs.controllers.department.components.CropImage;
import ku.cs.controllers.department.components.DefaultLabel;
import ku.cs.controllers.department.components.RouteButton;

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
        DefaultImage tanaananImage = new DefaultImage(tanaananImageView);
        DefaultImage rugsitImage = new DefaultImage(rugsitImageView);
        DefaultImage sirisukImage = new DefaultImage(sirisukImageView);
        DefaultImage narakornImage = new DefaultImage(narakornImageView);
        tanaananImage.setImage(tanaanan);
        rugsitImage.setImage(rugsit);
        sirisukImage.setImage(sirisuk);
        narakornImage.setImage(narakorn);
        tanaananImage.makeImageCircle();
        rugsitImage.makeImageCircle();
        sirisukImage.makeImageCircle();
        narakornImage.makeImageCircle();
    }
}


