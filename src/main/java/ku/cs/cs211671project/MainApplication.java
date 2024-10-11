package ku.cs.cs211671project;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ku.cs.services.FXRouter;
import ku.cs.services.Theme;

import java.awt.*;
import java.io.IOException;
public class MainApplication extends Application {
    public static final double windowWidth = 1280;
    public static final double windowHeight = 720;
    private Theme theme = Theme.getInstance();
    @Override
    public void start(Stage stage) throws IOException {
        String fontsPath = "/fonts/";
        Font.loadFont(getClass().getResourceAsStream(fontsPath + "PrintAble4U Regular ver 1.00.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream(fontsPath + "PrintAble4U Bold ver 1.00.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream(fontsPath + "PrintAble4U Italic ver 1.00.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream(fontsPath + "PrintAble4U Bold Italic ver 1.00.ttf"), 12);

        Font.loadFont(getClass().getResourceAsStream(fontsPath + "Krub Bold.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream(fontsPath + "Krub Regular.ttf"), 12);

        Font.loadFont(getClass().getResourceAsStream(fontsPath + "THSarabunNew Bold.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream(fontsPath + "THSarabunNew.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream(fontsPath + "PK Maehongson Round Demo.ttf"), 12);
        FXRouter.bind(this, stage, "MyRequestKU", windowWidth, windowHeight);

        configRoutes();
        stage.setResizable(false);
        Image logo16 = new Image(getClass().getResourceAsStream("/images/logos/application-logo16x16.png"));
        Image logo32 = new Image(getClass().getResourceAsStream("/images/logos/application-logo32x32.png"));
        Image logo48 = new Image(getClass().getResourceAsStream("/images/logos/application-logo48x48.png"));
        Image logo64 = new Image(getClass().getResourceAsStream("/images/logos/application-logo64x64.png"));
        Image logo128 = new Image(getClass().getResourceAsStream("/images/logos/application-logo128x128.png"));
        Image logo500 = new Image(getClass().getResourceAsStream("/images/logos/application-logo500x500.png"));
        stage.getIcons().addAll(logo16, logo32, logo48, logo64, logo128, logo500);

        if (System.getProperty("os.name").contains("Mac")) {
            Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
            java.awt.Image image = defaultToolkit.getImage(getClass().getResource("/images/logos/application-logo128x128.png"));
            Taskbar taskbar = Taskbar.getTaskbar();
            taskbar.setIconImage(image);
        }

        FXRouter.goTo("login");
        theme.setTheme("default");
    }
    private void configRoutes() {
        String viewPath = "ku/cs/views/";
        FXRouter.when("hello", viewPath + "hello-view.fxml");

        FXRouter.when("login", viewPath + "login-page.fxml");
        FXRouter.when("register", viewPath + "register-page.fxml");
        FXRouter.when("admin-manage-staff", viewPath + "admin-page-manage-staff.fxml");
        FXRouter.when("admin-manage-users", viewPath + "admin-page-manage-users.fxml");
        FXRouter.when("admin-manage-faculty-department", viewPath + "admin-page-manage-faculty-department.fxml");
        FXRouter.when("admin-user-profile", viewPath + "admin-page.fxml");
        FXRouter.when("student-page", viewPath + "student-page.fxml");
        FXRouter.when("advisor-students", viewPath + "advisor-students.fxml");
        //Department
        FXRouter.when("department-staff-request-list", viewPath + "department-staff-request-list.fxml");
        FXRouter.when("department-staff-approver-list", viewPath + "department-staff-approver-list.fxml");
        FXRouter.when("department-staff-nisit-advisor-management", viewPath + "department-staff-nisit-advisor-management.fxml");
        FXRouter.when("department-staff-nisit-management", viewPath + "department-staff-nisit-management.fxml");
        FXRouter.when("department-staff-add-nisit", viewPath + "department-staff-add-nisit.fxml");
        FXRouter.when("department-staff-profile", viewPath + "department-staff-profile.fxml");

        FXRouter.when("choose-request-form", viewPath + "choose-request-form-page.fxml");
        FXRouter.when("test", viewPath + "test-datasource.fxml");

        FXRouter.when("request-management", viewPath + "staff-request-management.fxml");



        // Faculty
        FXRouter.when("faculty-page", viewPath + "faculty-pages.fxml");
        FXRouter.when("faculty-approver", viewPath + "faculty-approver-page.fxml");
        FXRouter.when("faculty-request-manage", viewPath + "faculty-request-management.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}