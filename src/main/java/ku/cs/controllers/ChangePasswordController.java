package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Stack;

public class ChangePasswordController {
    @FXML
    private Button acceptButton;
    @FXML
    private Button exitButton; // Corrected fx:id to match FXML

    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField confirmPasswordTextField;
    @FXML
    private Label errorLabel;

    private Stage stage;

    public void setStage(Stage stage){this.stage = stage;}

    @FXML
    private void initialize() {
    }

    private boolean passwordChange() {
        String password1 = passwordTextField.getText().trim();
        String password2 = confirmPasswordTextField.getText().trim();

        if (password1.equals(password2)) {
            errorLabel.setVisible(false);
            System.out.println("Password change confirm");
            return true;
        } else {
            errorLabel.setVisible(true);
            errorLabel.setText("รหัสผ่านไม่ตรงกัน โปรดทำการกรอกรหัสผ่านใหม่อีกครั้ง");
            return false;
        }
    }


    @FXML
    private void onAcceptClick() {
        // Handle the accept button click event
        System.out.println("Accept button clicked");
        if (passwordChange()){
            stage.close();
        } else{
            System.out.println("Not finished undo closing !");
        }

    }

    @FXML
    private void onExitClick() {
        System.out.println("Exit button clicked");
        if (stage != null) {
            stage.close();
        }
    }

}
