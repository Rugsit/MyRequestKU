package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ChooseRequestFromController {
    @FXML
    public BorderPane borderPane;

    @FXML
    public void onBackButtonClick() {
        try {
            String viewPath = "/ku/cs/views/student-requests-pane.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            StudentRequestsController controller = fxmlLoader.getController();
            controller.setBorderPane(this.borderPane);
            borderPane.setCenter(pane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onGeneralRequestVBoxClick() {
        try {
            String viewPath = "/ku/cs/views/general-request-form-pane.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            GeneralRequestFormController controller = fxmlLoader.getController();
            controller.setBorderPane(this.borderPane);
            borderPane.setCenter(pane);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @FXML
    public void onRegisterRequestVBoxClick() {
        try {
            String viewPath = "/ku/cs/views/register-request-form-pane.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            RegisterRequestFormController controller = fxmlLoader.getController();
            controller.setBorderPane(this.borderPane);
            borderPane.setCenter(pane);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @FXML
    public void onAcademicLeaveRequestVBoxClick() {
        try {
            String viewPath = "/ku/cs/views/academic-leave-form-pane.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            AcademicLeaveController controller = fxmlLoader.getController();
            controller.setBorderPane(this.borderPane);
            borderPane.setCenter(pane);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }


}
