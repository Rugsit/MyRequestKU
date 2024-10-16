package ku.cs.controllers.faculty;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.controllers.AboutUsController;
import ku.cs.controllers.ParentController;
import ku.cs.controllers.SettingController;
import ku.cs.controllers.UserProfileCardController;
import ku.cs.models.user.FacultyUser;
import ku.cs.models.user.User;
import ku.cs.services.*;
import ku.cs.views.components.SquareImage;

import java.io.IOException;
import java.util.HashMap;

public class FacultyPageController implements Observer<HashMap<String, String>>, ParentController {
    @FXML
    Label tabAccountNameLabel;
    @FXML
    ImageView tabProfilePicImageView;
    @FXML
    BorderPane contentBorderPane;
    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private Button settingButton;
    @FXML
    private Button requestButton;
    @FXML
    private Button approveButton;
    private FacultyUser loginUser;
    ImageDatasource datasource;

    public void initialize(){
        updateStyle();

        SetTransition.setButtonBounce(settingButton);
        SetTransition.setButtonBounce(requestButton);
        SetTransition.setButtonBounce(approveButton);

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
            controller.setMainCSS(getClass().getResource("/ku/cs/styles/general-dark.css").toString()
                    , getClass().getResource("/ku/cs/styles/general.css").toString());

            currentPopupStage.setScene(scene);
            currentPopupStage.initModality(Modality.APPLICATION_MODAL);
            currentPopupStage.setTitle("ตั้งค่า");
            currentPopupStage.setResizable(false);
            addImageToPopup(currentPopupStage);
            currentPopupStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateStyle() {
        Theme.getInstance().loadCssToPage(mainAnchorPane, new PathGenerator() {
            @Override
            public String getThemeDarkPath() {
                return getClass().getResource("/ku/cs/styles/general-dark.css").toString();
            }
            @Override
            public String getThemeLightPath() {
                return getClass().getResource("/ku/cs/styles/general.css").toString();
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

    private void addImageToPopup(Stage currentPopupStage) {
        Image logo16 = new Image(getClass().getResourceAsStream("/images/logos/application-logo16x16.png"));
        Image logo32 = new Image(getClass().getResourceAsStream("/images/logos/application-logo32x32.png"));
        Image logo48 = new Image(getClass().getResourceAsStream("/images/logos/application-logo48x48.png"));
        Image logo64 = new Image(getClass().getResourceAsStream("/images/logos/application-logo64x64.png"));
        Image logo128 = new Image(getClass().getResourceAsStream("/images/logos/application-logo128x128.png"));
        Image logo500 = new Image(getClass().getResourceAsStream("/images/logos/application-logo500x500.png"));
        currentPopupStage.getIcons().addAll(logo16, logo32, logo48, logo64, logo128, logo500);
    }
}
