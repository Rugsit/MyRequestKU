package ku.cs.controllers.student;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import ku.cs.models.user.User;
import ku.cs.views.components.CropImage;

public class StudentProfileController {
    @FXML ImageView profilePictureImageView;
    @FXML
    public Label usernameLabel;
    @FXML
    public Label studentNameLabel;
    @FXML
    public Label facultyLabel;
    @FXML
    public Label departmentLabel;
    @FXML
    public Label userTypeLabel;
    @FXML
    public Label idLabel;
    @FXML
    public Label advisorLabel;
    private User loginUser;
    public CropImage profilePicture;


    public void initialize(){
        profilePicture = new CropImage(profilePictureImageView);
    }

}
