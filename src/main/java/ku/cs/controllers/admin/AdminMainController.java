package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import ku.cs.controllers.ParentController;
import ku.cs.controllers.UserProfileCardController;
import ku.cs.controllers.student.StudentRequestsController;
import ku.cs.models.user.Admin;
import ku.cs.models.user.Advisor;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.services.*;
import ku.cs.views.components.SquareImage;

import java.io.IOException;
import java.util.Set;

public class AdminMainController implements ParentController {
    private Datasource<UserList> datasource;

    private Admin loginUser;

    private UserList userList;

    @FXML
    private BorderPane borderPane;
    @FXML
    private ImageView tabProfilePicImageView;
    @FXML
    private Label nameLabel;
    @FXML
    private Button facultyButton;
    @FXML
    private Button dashboardButton;
    @FXML
    private Button userButton;
    @FXML
    private Button staffButton;


    @FXML
    public void initialize() {
        SetTransition transition = new SetTransition();
        transition.setClickChangeColor(facultyButton, "#374957" , "#FFFFFF", "faculty-white-icon.png");
        transition.setClickChangeColor(dashboardButton, "#374957" , "#FFFFFF", "chart-white-icon.png");
        transition.setClickChangeColor(userButton, "#374957" , "#FFFFFF", "many-people-white-icon.png");
        transition.setClickChangeColor(staffButton, "#374957" , "#FFFFFF", "people-white-icon.png");


        if (FXRouter.getData() instanceof Admin) {
            loginUser = (Admin) FXRouter.getData();
        }
        datasource = new UserListFileDatasource("data", "admin.csv");
        userList = ((UserListFileDatasource)datasource).readAllUser();
        nameLabel.setText(loginUser.getName());

        try {
            String viewPath = "/ku/cs/views/admin-profile-pane.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            AdminProfileController controller = fxmlLoader.getController();
            controller.setLoginUser(loginUser);
            controller.showDataOnCard();
            borderPane.setCenter(pane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        gotoProfilePage();
        loadProfile();
    }

    @FXML
    protected void goToAdminManageStaff() {
        try {
            String viewPath = "/ku/cs/views/admin-pane-manage-staff.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            AdminManageStaffController controller = fxmlLoader.getController();
            controller.setLoginUser(loginUser);
            controller.initialize();
            borderPane.setCenter(pane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void goToAdminManageFaculty() {
        try {
            String viewPath = "/ku/cs/views/admin-pane-manage-faculty-department.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            AdminManageFacultyController controller = fxmlLoader.getController();
            controller.setLoginUser(loginUser);
            controller.initialize();
            borderPane.setCenter(pane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void goToAdminManageUsers() {
        try {
            String viewPath = "/ku/cs/views/admin-pane-manage-users.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            AdminManageUsersController controller = fxmlLoader.getController();
            controller.setLoginUser(loginUser);
            controller.initializeUser();
            borderPane.setCenter(pane);
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
    private void onAboutUsClicked() {
        try {
            String viewPath = "/ku/cs/views/about-us-pane.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            borderPane.setCenter(pane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void gotoProfilePage() {
        try {
            String viewPath = "/ku/cs/views/user-profile-card.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            UserProfileCardController controller = fxmlLoader.getController();
            controller.setLoginUser(loginUser);
            controller.setParentController(this);
            controller.initialize();
            borderPane.setCenter(pane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void gotoDashBoard() {
        try {
            String viewPath = "/ku/cs/views/admin-dashboard.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            DashBoardController controller = fxmlLoader.getController();
            controller.setLoginUser(loginUser);
//            controller.setParentController(this);
            controller.initializeDashBoard();
            borderPane.setCenter(pane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setLoginUser(User loginUser) {
        if (loginUser == null) {return;}
        if (loginUser instanceof Admin) {
            this.loginUser = (Admin)loginUser;
        }
    }

    @Override
    public void loadProfile() {
        ImageDatasource datasource = new ImageDatasource("users");
        SquareImage profilePic = new SquareImage(tabProfilePicImageView);
        profilePic.setClipImage(150,150);
        if (loginUser.getAvatar().equalsIgnoreCase("no-image")) {
            profilePic.setImage(new Image(getClass().getResourceAsStream("/images/no-img.png")));
        } else {
            profilePic.setImage(datasource.openImage(loginUser.getAvatar()));
        }
    }
}
