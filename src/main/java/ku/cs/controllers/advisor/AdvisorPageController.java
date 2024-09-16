package ku.cs.controllers.advisor;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import ku.cs.controllers.student.StudentRequestsController;
import ku.cs.models.user.Advisor;
import ku.cs.services.FXRouter;
import ku.cs.services.ImageDatasource;
import ku.cs.views.components.SquareImage;

import java.io.IOException;

public class AdvisorPageController {
    @FXML Circle imageCircle;
    @FXML Label tabAccountNameLabel;
    @FXML ImageView tabProfilePicImageView;
    @FXML BorderPane contentBorderPane;
    private Advisor loginUser;
    ImageDatasource datasource;

    public void initialize(){
        if (FXRouter.getData() instanceof Advisor)
        {
            loginUser = (Advisor) FXRouter.getData();
        }
        //Image profile = new Image(getClass().getResourceAsStream("/images/users/side-bar-profile.png"));
        //imageCircle.setFill(new ImagePattern(profile));
        datasource = new ImageDatasource("users");
        SquareImage profilePic = new SquareImage(tabProfilePicImageView);
        profilePic.setClipImage(150, 150);
        profilePic.setImage(datasource.openImage(loginUser.getAvatar()));

        onStudentClicked();
        tabAccountNameLabel.setText(loginUser.getName());
    }





    @FXML
    protected void onStudentClicked() {
        try {
            String viewPath = "/ku/cs/views/advisor-students-pane.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            AdvisorStudentListController controller = fxmlLoader.getController();
            controller.setBorderPane(this.contentBorderPane);
            contentBorderPane.setCenter(pane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onRequestsClicked(){
        try {
            String viewPath = "/ku/cs/views/advisor-requests-pane.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            AdvisorRequestsController controller = fxmlLoader.getController();
            controller.setBorderPane(this.contentBorderPane);
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

    @FXML
    public void onSideProfileClicked(){
        try {
            String viewPath = "/ku/cs/views/advisor-profile-card.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            AdvisorProfileController advisorProfileController = fxmlLoader.getController();
            advisorProfileController.usernameLabel.setText(loginUser.getUsername());
            advisorProfileController.departmentLabel.setText(loginUser.getDepartment());
            advisorProfileController.idLabel.setText(loginUser.getId());
            advisorProfileController.studentNameLabel.setText(loginUser.getName());
            advisorProfileController.facultyLabel.setText(loginUser.getFaculty());
            advisorProfileController.profilePicture.setImage(datasource.openImage(loginUser.getAvatar()));
            advisorProfileController.profilePicture.setClipImage(50,50);
            advisorProfileController.userTypeLabel.setText(loginUser.getRole());
            advisorProfileController.advisorLabel.setText("None");


            contentBorderPane.setCenter(pane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}





