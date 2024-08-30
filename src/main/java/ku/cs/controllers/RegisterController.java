package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class RegisterController {
    @FXML private TextField usernameTextField;

    @FXML
    public void initialize() {
        usernameTextField.requestFocus();
    }

    @FXML
        protected void goToLogin() {
        try {
            FXRouter.goTo("login");
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER){
            System.out.println("register");
        }
    }
}
