package ku.cs.cs211671project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class MainApplication extends Application {
    public static final double windowWidth = 1280;
    public static final double windowHeight = 720;
    @Override
    public void start(Stage stage) throws IOException {
        String fontsPath = "/fonts/";
        Font.loadFont(getClass().getResourceAsStream(fontsPath + "PrintAble4U Regular ver 1.00.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream(fontsPath + "PrintAble4U Bold ver 1.00.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream(fontsPath + "PrintAble4U Italic ver 1.00.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream(fontsPath + "PrintAble4U Bold Italic ver 1.00.ttf"), 12);
        FXRouter.bind(this, stage, "CS211 Project", windowWidth, windowHeight);
        configRoutes();

        FXRouter.goTo("login");
    }
    private void configRoutes() {
        String viewPath = "ku/cs/views/";
        FXRouter.when("hello", viewPath + "hello-view.fxml");

        FXRouter.when("login", viewPath + "login-page.fxml");
        FXRouter.when("register", viewPath + "register-page.fxml");
        FXRouter.when("admin-manage-staff", viewPath + "admin-page-manage-staff.fxml");
        FXRouter.when("admin-manage-users", viewPath + "admin-page-manage-users.fxml");
        FXRouter.when("admin-manage-faculty-department", viewPath + "admin-page-manage-faculty-department.fxml");
        FXRouter.when("user-profile", viewPath + "admin-profile-page.fxml");
        FXRouter.when("student-page", viewPath + "student-page.fxml");
        FXRouter.when("advisor-requests", viewPath + "advisor-requests.fxml");
        FXRouter.when("advisor-students", viewPath + "advisor-students.fxml");
        //Department
        FXRouter.when("department-staff-request-list", viewPath + "department-staff-request-list.fxml");
        FXRouter.when("department-staff-request", viewPath + "department-staff-request.fxml");
        FXRouter.when("department-staff-approver-list", viewPath + "department-staff-approver-list.fxml");
        FXRouter.when("department-staff-nisit-advisor-management", viewPath + "department-staff-nisit-advisor-management.fxml");
        FXRouter.when("department-staff-nisit-management", viewPath + "department-staff-nisit-management.fxml");





        // Faculty
        FXRouter.when("faculty-requests", viewPath + "faculty-requests.fxml");
        FXRouter.when("faculty-approver", viewPath + "faculty-approver.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}