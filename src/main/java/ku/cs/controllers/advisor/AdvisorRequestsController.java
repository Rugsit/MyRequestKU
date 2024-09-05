package ku.cs.controllers.advisor;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class AdvisorRequestsController {
    @FXML Circle imageCircle;

    public void initialize(){
        Image profile = new Image(getClass().getResourceAsStream("/images/users/side-bar-profile.png"));
        imageCircle.setFill(new ImagePattern(profile));
    }


    @FXML
    protected void goToStudentList() {
        try {
            FXRouter.goTo("advisor-students");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onLogoutClicked() {
        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}





