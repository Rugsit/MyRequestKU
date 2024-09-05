package ku.cs.controllers.requests;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ku.cs.controllers.student.StudentRequestsController;
import ku.cs.models.request.Request;

import java.io.IOException;

public class ConfirmRequestFormController {
    Stage stage;

    Request request;
    Request requestPair;

    @FXML
    public BorderPane borderPane;

    @FXML
    public void onEditClick() {
        stage.close();
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    public void setRequestForm(Request request) {
        this.request = request;
    }

    public void setRequestPair(Request request) {
        this.requestPair = request;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void onConfirmClick() {
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
        System.out.println(this.request);
        if (this.request != null) {
            System.out.println(this.requestPair);
        }
        stage.close();
    }
}
