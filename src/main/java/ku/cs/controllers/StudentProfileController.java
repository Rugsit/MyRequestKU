package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class StudentProfileController {
    @FXML ImageView profilePictureImageView;
    @FXML Circle tabProfilePicCircle;
    @FXML Label tabAccountNameLabel;
    @FXML Label usernameLabel;
    @FXML Label studentNameLabel;
    @FXML Label facultyLabel;
    @FXML Label departmentLabel;
    @FXML Label userTypeLabel;
    @FXML Label idLabel;
    @FXML Label advisorLabel;


    public void initialize(){
        showInfo();
    }

    private void showInfo(){
        Image sideProfilePicture = new Image(getClass().getResourceAsStream("/images/users/side-bar-profile.png"));
        Image profilePicture = new Image(getClass().getResourceAsStream("/images/users/card-profile.png"));
        profilePictureImageView.setImage(profilePicture);
        tabProfilePicCircle.setFill(new ImagePattern(sideProfilePicture));
        tabAccountNameLabel.setText("นายเมมโมรี่โฟม รักนอน");
        usernameLabel.setText("MemoryF");
        studentNameLabel.setText("เมมโมรี่โฟม รักนอน");
        facultyLabel.setText("คณะวิทยาศาสตร์");
        departmentLabel.setText("ภาควิชาวิทยาการคอมพิวเตอร์");
        userTypeLabel.setText("นิสิต");
        idLabel.setText("6610407777");
        advisorLabel.setText("ผศ.ดร.โพลีเอสเตอร์ นอนไม่พอ");
    }

    @FXML
    public void onRequestsButtonClicked(){
        try{
            FXRouter.goTo("student-requests");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
