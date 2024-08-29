package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.util.Stack;

public class ErrorGeneralRequestFormController {

    Stage stage;
    @FXML
    public void onAgreeClick() {
        stage.close();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
