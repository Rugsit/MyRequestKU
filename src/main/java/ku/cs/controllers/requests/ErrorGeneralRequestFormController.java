package ku.cs.controllers.requests;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ErrorGeneralRequestFormController {

    Stage stage;
    @FXML
    private Label errorMessage;
    @FXML
    public void onAgreeClick() {
        stage.close();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage.setText(errorMessage);
    }
}
