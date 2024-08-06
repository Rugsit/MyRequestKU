package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class LoginController {
    @FXML private TextField userNameTextField;
    @FXML private Button selectAdminRoleButton;
    @FXML private Button selectFacultyStaffRoleButton;
    @FXML private Button selectDepartmentStaffRoleButton;
    @FXML private Button selectAdviserRoleButton;
    @FXML private Button selectStudentRoleButton;

    @FXML
    protected void OnLoginButtonClick() {
        if (userNameTextField.getText().trim().equalsIgnoreCase("debug")) {
            selectAdminRoleButton.setVisible(true);
            selectFacultyStaffRoleButton.setVisible(true);
            selectDepartmentStaffRoleButton.setVisible(true);
            selectAdviserRoleButton.setVisible(true);
            selectStudentRoleButton.setVisible(true);
        } else {
            selectAdminRoleButton.setVisible(false);
            selectFacultyStaffRoleButton.setVisible(false);
            selectDepartmentStaffRoleButton.setVisible(false);
            selectAdviserRoleButton.setVisible(false);
            selectStudentRoleButton.setVisible(false);
        }
    }

    @FXML
    protected void goToRegister() {
        try {
            FXRouter.goTo("register");
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
    }
}
