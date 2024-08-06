package ku.cs.cs211671project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXRouter.bind(this, stage, "CS211 Project", 1280, 720);
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
        FXRouter.when("user-profile", viewPath + "user-profile-page.fxml");

    }

    public static void main(String[] args) {
        launch();
    }
}