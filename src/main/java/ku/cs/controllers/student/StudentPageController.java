package ku.cs.controllers.student;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.controllers.ParentController;
import ku.cs.controllers.SettingController;
import ku.cs.controllers.UserProfileCardController;
import ku.cs.models.user.Student;
import ku.cs.models.user.User;
import ku.cs.services.ImageDatasource;
import ku.cs.services.PathGenerator;
import ku.cs.services.Theme;
import ku.cs.views.components.SquareImage;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class StudentPageController implements ParentController {
    @FXML private BorderPane contentBorderPane;
    @FXML private ImageView tabProfilePicImageView;
    @FXML private Label tabAccountNameLabel;
    @FXML private AnchorPane mainAnchorPane;

    private Student loginUser;
    ImageDatasource datasource;

    @FXML
    public void initialize() {
        updateStyle();

        if (FXRouter.getData() instanceof Student)
        {
            loginUser = (Student)FXRouter.getData();
        }
        loadProfile();
        onRequestsButtonClicked();
        tabAccountNameLabel.setText(loginUser.getName());
    }

    public void loadProfile() {
        datasource = new ImageDatasource("users");
        SquareImage profilePic = new SquareImage(tabProfilePicImageView);
        profilePic.setClipImage(150,150);
        profilePic.setImage(datasource.openImage(loginUser.getAvatar()));
    }
    @FXML
    public void onRequestsButtonClicked(){
        try {
            String viewPath = "/ku/cs/views/student-requests-pane.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            StudentRequestsController controller = fxmlLoader.getController();
            controller.setLoginUser(loginUser);
            controller.initialize();
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

    public void setLoginUser(User loginUser) {
        if (loginUser == null) {return;}
        if (loginUser instanceof Student) {
            this.loginUser = (Student)loginUser;
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
}
