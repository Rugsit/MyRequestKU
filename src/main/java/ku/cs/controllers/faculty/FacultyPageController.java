package ku.cs.controllers.faculty;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.controllers.ParentController;
import ku.cs.controllers.SettingController;
import ku.cs.controllers.UserProfileCardController;
import ku.cs.controllers.student.StudentRequestsController;
import ku.cs.models.faculty.Faculty;
import ku.cs.models.user.Advisor;
import ku.cs.models.user.FacultyUser;
import ku.cs.models.user.User;
import ku.cs.services.*;
import ku.cs.views.components.SquareImage;

import java.io.IOException;
import java.util.HashMap;

public class FacultyPageController implements Observer<HashMap<String, String>>, ParentController {
    @FXML
    Circle imageCircle;
    @FXML
    Label tabAccountNameLabel;
    @FXML
    ImageView tabProfilePicImageView;
    @FXML
    BorderPane contentBorderPane;
    @FXML
    private AnchorPane mainAnchorPane;
    private FacultyUser loginUser;
    ImageDatasource datasource;

    public void initialize(){
        updateStyle();

        if (FXRouter.getData() instanceof FacultyUser){
            setLoginUser((User) FXRouter.getData());
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
            controller.setParentController(this);
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

    @FXML
    public void onApproverButtonClicked() {
        try {
            FXRouter.goTo("faculty-approver", loginUser);
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onAboutUsClicked() {

    }

    @FXML
    private void goToSetting() {
        try {
            Stage currentPopupStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/setting.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            SettingController controller = fxmlLoader.getController();
            controller.setMainAnchorPane(mainAnchorPane);
            controller.setStage(currentPopupStage);
            controller.setMainCSS(getClass().getResource("/ku/cs/styles/admin-page-style-dark.css").toString()
                    , getClass().getResource("/ku/cs/styles/admin-page-style.css").toString());

            scene.getStylesheets().add(getClass().getResource("/ku/cs/styles/error-confirm-edit-page-style.css").toExternalForm());
            currentPopupStage.setScene(scene);
            currentPopupStage.initModality(Modality.APPLICATION_MODAL);
            currentPopupStage.setTitle("Setting");
            currentPopupStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateStyle() {
        Theme.getInstance().loadCssToPage(mainAnchorPane, new PathGenerator() {
            @Override
            public String getThemeDarkPath() {
                return getClass().getResource("/ku/cs/styles/admin-page-style-dark.css").toString();
            }
            @Override
            public String getThemeLightPath() {
                return getClass().getResource("/ku/cs/styles/admin-page-style.css").toString();
            }
        });
    }

    @Override
    public void update(HashMap<String, String> data) {
        mainAnchorPane.setStyle(mainAnchorPane.getStyle()+"-fx-background-color: " + data.get("secondary") + ";");
    }


    @Override
    public void setLoginUser(User loginUser) {
        this.loginUser = (FacultyUser) loginUser;
    }

    @Override
    public void loadProfile() {
        datasource = new ImageDatasource("users");
        SquareImage profilePic = new SquareImage(tabProfilePicImageView);
        profilePic.setClipImage(150, 150);
        profilePic.setImage(datasource.openImage(loginUser.getAvatar()));
    }
}
