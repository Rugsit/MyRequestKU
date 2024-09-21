package ku.cs.controllers.faculty;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import ku.cs.controllers.UserProfileCardController;
import ku.cs.controllers.student.StudentRequestsController;
import ku.cs.models.faculty.Faculty;
import ku.cs.models.user.Advisor;
import ku.cs.models.user.FacultyUser;
import ku.cs.services.FXRouter;
import ku.cs.services.ImageDatasource;
import ku.cs.views.components.SquareImage;

import java.io.IOException;

public class FacultyPageController {
    @FXML
    Circle imageCircle;
    @FXML
    Label tabAccountNameLabel;
    @FXML
    ImageView tabProfilePicImageView;
    @FXML
    BorderPane contentBorderPane;
    private static FacultyUser loginUser;
    ImageDatasource datasource;

    public void initialize(){
        if (FXRouter.getData() instanceof FacultyUser){
            loginUser = (FacultyUser) FXRouter.getData();
        }

        datasource = new ImageDatasource("users");
        SquareImage profilePic = new SquareImage(tabProfilePicImageView);
        profilePic.setClipImage(150, 150);
        profilePic.setImage(datasource.openImage(loginUser.getAvatar()));
        tabAccountNameLabel.setText(loginUser.getName());
        onRequestsButtonClicked();
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
            String viewPath = "/ku/cs/views/user-profile-card.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            UserProfileCardController controller = fxmlLoader.getController();
            controller.setLoginUser(loginUser);
            //controller.setParentController(this);
            controller.initialize();
            contentBorderPane.setCenter(pane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onRequestsButtonClicked(){
        try {
            String viewPath = "/ku/cs/views/faculty-requests-pane.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            FacultyRequestsController controller = fxmlLoader.getController();
            controller.setLoginUser(loginUser);
            controller.initialize();
            controller.setBorderPane(this.contentBorderPane);
            contentBorderPane.setCenter(pane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
