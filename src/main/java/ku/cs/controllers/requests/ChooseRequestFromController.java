package ku.cs.controllers.requests;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import ku.cs.controllers.student.StudentRequestsController;
import ku.cs.models.user.Student;
import ku.cs.models.user.User;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class ChooseRequestFromController {
    private User loginUser;

    @FXML
    public BorderPane borderPane;

    @FXML
    private void initialize() {
        if (FXRouter.getData() instanceof User) {
            loginUser = (User) FXRouter.getData();
        }
    }

    @FXML
    public void onBackButtonClick() {
        try {
            String viewPath = "/ku/cs/views/student-requests-pane.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            StudentRequestsController controller = fxmlLoader.getController();
            controller.setBorderPane(this.borderPane);
            controller.setLoginUser((Student) loginUser);
            controller.showTable();
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
            controller.setLoginUser(loginUser);
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
            controller.setLoginUser(loginUser);
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
            controller.setLoginUser(loginUser);
            borderPane.setCenter(pane);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }
    public void setLoginUser(User loginUser) {
        this.loginUser = loginUser;
    }


}
