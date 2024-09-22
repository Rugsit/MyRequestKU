package ku.cs.controllers.advisor;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import ku.cs.controllers.ParentController;
import ku.cs.controllers.UserProfileCardController;
import ku.cs.controllers.student.StudentRequestsController;
import ku.cs.models.user.Advisor;
import ku.cs.models.user.Student;
import ku.cs.models.user.User;
import ku.cs.services.FXRouter;
import ku.cs.services.ImageDatasource;
import ku.cs.views.components.SquareImage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class AdvisorPageController implements ParentController {
    @FXML Circle imageCircle;
    @FXML Label tabAccountNameLabel;
    @FXML ImageView tabProfilePicImageView;
    @FXML BorderPane contentBorderPane;
    private static Advisor loginUser;
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

    public static UUID getAdvisorUUID() {
        return loginUser.getUUID();
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
    public void onSideProfileClicked() {
        try {
            String viewPath = "/ku/cs/views/user-profile-card.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            UserProfileCardController controller = fxmlLoader.getController();
            controller.setLoginUser(loginUser);
            controller.setParentController(this);
            controller.initialize();
            contentBorderPane.setCenter(pane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setLoginUser(User loginUser) {
        if (loginUser == null) {return;}
        if (loginUser instanceof Advisor) {
            loginUser = (Advisor)loginUser;
        }
    }

    @Override
    public void loadProfile() {
        datasource = new ImageDatasource("users");
        SquareImage profilePic = new SquareImage(tabProfilePicImageView);
        profilePic.setClipImage(150,150);
        profilePic.setImage(datasource.openImage(loginUser.getAvatar()));
    }
}





