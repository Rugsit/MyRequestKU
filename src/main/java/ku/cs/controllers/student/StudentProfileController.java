package ku.cs.controllers.student;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ku.cs.models.user.User;
import ku.cs.views.components.CropImage;

public class StudentProfileController {
    @FXML ImageView profilePictureImageView;
    @FXML Label usernameLabel;
    @FXML Label studentNameLabel;
    @FXML Label facultyLabel;
    @FXML Label departmentLabel;
    @FXML Label userTypeLabel;
    @FXML Label idLabel;
    @FXML Label advisorLabel;
    private User loginUser;
    CropImage profilePicture;


    public void initialize(){
        profilePicture = new CropImage(profilePictureImageView);
    }

}
