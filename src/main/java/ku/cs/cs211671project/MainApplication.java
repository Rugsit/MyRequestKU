package ku.cs.cs211671project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        String fontsPath = "/fonts/";
        Font.loadFont(getClass().getResourceAsStream(fontsPath + "PrintAble4U Regular ver 1.00.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream(fontsPath + "PrintAble4U Bold ver 1.00.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream(fontsPath + "PrintAble4U Italic ver 1.00.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream(fontsPath + "PrintAble4U Bold Italic ver 1.00.ttf"), 12);
        FXRouter.bind(this, stage, "CS211 Project", 1280, 720);
        configRoutes();
        FXRouter.goTo("department-staff-request-list");
    }

    private void configRoutes() {
        String viewPath = "ku/cs/views/";
        FXRouter.when("hello", viewPath + "hello-view.fxml");
        FXRouter.when("department-staff-request-list", viewPath + "department-staff-request-list.fxml");
        FXRouter.when("department-staff-request", viewPath + "department-staff-request.fxml");
        FXRouter.when("department-staff-approver-list", viewPath + "department-staff-approver-list.fxml");
        FXRouter.when("department-staff-nisit-advisor-management", viewPath + "department-staff-nisit-advisor-management.fxml");
        FXRouter.when("department-staff-nisit-management", viewPath + "department-staff-nisit-management.fxml");

    }

    public static void main(String[] args) {
        launch();
    }
}