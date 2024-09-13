package ku.cs.controllers.student;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import ku.cs.models.user.Student;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.services.Datasource;
import ku.cs.services.ImageDatasource;
import ku.cs.services.UserListFileDatasource;
import ku.cs.views.components.SquareImage;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class StudentPageController {
    @FXML private BorderPane contentBorderPane;
    @FXML private ImageView tabProfilePicImageView;
    @FXML private Label tabAccountNameLabel;
    private Student loginUser;
    ImageDatasource datasource;

    @FXML
    public void initialize() {
        if (FXRouter.getData() instanceof Student)
        {
            loginUser = (Student)FXRouter.getData();
        }
        datasource = new ImageDatasource("users");
        SquareImage profilePic = new SquareImage(tabProfilePicImageView);
        profilePic.setClipImage(150,150);
        profilePic.setImage(datasource.openImage(loginUser.getAvatar()));

        onRequestsButtonClicked();
        tabAccountNameLabel.setText(loginUser.getName());
    }

    @FXML
    public void onRequestsButtonClicked(){
        try {
            String viewPath = "/ku/cs/views/student-requests-pane.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            StudentRequestsController controller = fxmlLoader.getController();
            controller.setBorderPane(this.contentBorderPane);
            contentBorderPane.setCenter(pane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onAboutUsClicked() {
        try {
            String viewPath = "/ku/cs/views/about-us-pane.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            contentBorderPane.setCenter(pane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onSideProfileClicked(){
        try {
            String viewPath = "/ku/cs/views/student-profile-card.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            StudentProfileController studentProfileController = fxmlLoader.getController();
            studentProfileController.usernameLabel.setText(loginUser.getUsername());
            studentProfileController.departmentLabel.setText(loginUser.getDepartment());
            studentProfileController.idLabel.setText(loginUser.getId());
            studentProfileController.studentNameLabel.setText(loginUser.getName());
            studentProfileController.facultyLabel.setText(loginUser.getFaculty());
            studentProfileController.profilePicture.setImage(datasource.openImage(loginUser.getAvatar()));
            studentProfileController.profilePicture.setClipImage(50,50);
            studentProfileController.userTypeLabel.setText(loginUser.getRole());
            studentProfileController.advisorLabel.setText("None");

            contentBorderPane.setCenter(pane);
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
