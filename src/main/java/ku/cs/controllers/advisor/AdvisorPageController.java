package ku.cs.controllers.advisor;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.controllers.AboutUsController;
import ku.cs.controllers.ParentController;
import ku.cs.controllers.SettingController;
import ku.cs.controllers.UserProfileCardController;
import ku.cs.models.user.Advisor;
import ku.cs.models.user.User;
import ku.cs.services.*;
import ku.cs.views.components.SquareImage;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class AdvisorPageController implements ParentController {
    @FXML Circle imageCircle;
    @FXML Label tabAccountNameLabel;
    @FXML ImageView tabProfilePicImageView;
    @FXML BorderPane contentBorderPane;
    @FXML private Button settingButton;
    @FXML private Button requestButton;
    @FXML private Button studentButton;

    private Advisor loginUser;
    private ImageDatasource datasource;
    @FXML AnchorPane mainAnchorPane;

    public void initialize(){
        updateStyle();

        SetTransition.setButtonBounce(settingButton);
        SetTransition.setButtonBounce(requestButton);
        SetTransition.setButtonBounce(studentButton);
        if (FXRouter.getData() instanceof Advisor) {
            setLoginUser((Advisor) FXRouter.getData());
        }

        if (FXRouter.getData() instanceof Advisor) {
            loginUser = (Advisor) FXRouter.getData();
        }
        datasource = new ImageDatasource("users");
        SquareImage profilePic = new SquareImage(tabProfilePicImageView);
        profilePic.setClipImage(150, 150);
        profilePic.setImage(datasource.openImage(loginUser.getAvatar()));

        onSideProfileClicked();
        tabAccountNameLabel.setText(loginUser.getName());
    }

    public UUID getAdvisorUUID() {
        return loginUser != null ? loginUser.getUUID() : null;
    }

    @FXML
    public void onStudentClicked() {
        try {
            String viewPath = "/ku/cs/views/advisor-students-pane.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();

            AdvisorStudentListController controller = fxmlLoader.getController();
            controller.setBorderPane(this.contentBorderPane);
            controller.setAdvisorPageController(this);
            controller.initializeController();

            contentBorderPane.setCenter(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    public void onRequestsClicked() {
        try {
            String viewPath = "/ku/cs/views/advisor-requests-pane.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            AdvisorRequestsController controller = fxmlLoader.getController();
            controller.setBorderPane(this.contentBorderPane);
            controller.setAdvisorPageController(this);
            controller.initializeRequest();

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
        if (loginUser instanceof Advisor) {
            this.loginUser = (Advisor) loginUser;
            tabAccountNameLabel.setText(loginUser.getName());
            loadProfile();
        }
    }

    @Override
    public void loadProfile() {
        datasource = new ImageDatasource("users");
        SquareImage profilePic = new SquareImage(tabProfilePicImageView);
        profilePic.setClipImage(150,150);
        profilePic.setImage(datasource.openImage(loginUser.getAvatar()));
    }

    @FXML
    public void onAboutUsClicked() {
        try {
            String viewPath = "/ku/cs/views/about-us-pane.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            AboutUsController controller = fxmlLoader.getController();
            controller.setLoginUser(loginUser);
            controller.initialize();
            contentBorderPane.setCenter(pane);
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





