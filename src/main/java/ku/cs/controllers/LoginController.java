package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ku.cs.controllers.department.components.DefaultLabel;
import ku.cs.models.user.User;
import ku.cs.services.FXRouter;
import org.w3c.dom.Text;

import java.io.IOException;

public class LoginController {
    @FXML private TextField userNameTextField;
    @FXML private TextField passwordTextField;
    @FXML private Label errorLabel;
    @FXML private Button selectAdminRoleButton;
    @FXML private Button selectFacultyStaffRoleButton;
    @FXML private Button selectDepartmentStaffRoleButton;
    @FXML private Button selectAdviserRoleButton;
    @FXML private Button selectStudentRoleButton;
    @FXML private Label aboutUsLabel;

    private AuthenticationController authController;

    @FXML
    private void initialize() {
        errorLabel.setText("");
        DefaultLabel aboutUs = new DefaultLabel(aboutUsLabel);
        authController = new AuthenticationController();
    }
    @FXML
    protected void onLoginButtonClick() {
        String username = userNameTextField.getText().trim();
        String password = passwordTextField.getText().trim();
        User loginUser = null;
        boolean isUseridInDatasource = authController.isUserInDatasource(username);
        try {
            loginUser = authController.loginAuthenticate(username, password);
        } catch (Exception e) {
            errorLabel.setText("รหัสผ่านไม่ถูกต้อง");
        }

        // Debug login method
        if (username.equalsIgnoreCase("debug")) {
            password = "debug"; // prevent warning display
            selectAdminRoleButton.setVisible(true);
            selectFacultyStaffRoleButton.setVisible(true);
            selectDepartmentStaffRoleButton.setVisible(true);
            selectAdviserRoleButton.setVisible(true);
            selectStudentRoleButton.setVisible(true);
            hideError();
        }
        else{
            selectAdminRoleButton.setVisible(false);
            selectFacultyStaffRoleButton.setVisible(false);
            selectDepartmentStaffRoleButton.setVisible(false);
            selectAdviserRoleButton.setVisible(false);
            selectStudentRoleButton.setVisible(false);
        }

        // Normal login method.
        if (loginUser != null){
            hideError();
            if (loginUser.getRole().equalsIgnoreCase("faculty")) {goToFacultyManage();}
            else if (loginUser.getRole().equalsIgnoreCase("admin")) {goToAdminManage();}
            else if (loginUser.getRole().equalsIgnoreCase("student")){onStudentButtonClicked();}
            else if (loginUser.getRole().equalsIgnoreCase("advisor")){goToAdvisorManage();}
            else if (loginUser.getRole().equalsIgnoreCase("department")){goToDepartmentManage();}
        } else if (!username.isEmpty() && !password.isEmpty() && !isUseridInDatasource) {
            if (!username.equalsIgnoreCase("debug") && !password.equalsIgnoreCase("debug")) {
                showError("ไม่พบข้อมูลผู้ใช้งานในระบบ กรุณาลงทะเบียน");
            }
        } else if (isUseridInDatasource){
            showError("รหัสผ่านไม่ถูกต้อง กรุณากรอกรหัสผ่านใหม่");
        }
        else if (!username.isEmpty() && password.isEmpty()) {
            showError("โปรดกรอกรหัสผ่าน");
        } else if (username.isEmpty() && password.isEmpty()) {
            showError("โปรดกรอกชื่อผู้ใช้ และรหัสผ่าน");
        }

    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void hideError() {
        errorLabel.setText("");
        errorLabel.setVisible(false);
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

    @FXML
    protected void goToAdminManage() {
        try {
            FXRouter.goTo("admin-manage-users");
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onStudentButtonClicked(){
        try{
            FXRouter.goTo("student-page");
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void goToAdvisorManage() {
        try {
            FXRouter.goTo("advisor-students");
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML protected void goToDepartmentManage() {
        try {
            FXRouter.goTo("department-staff-request-list");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void goToFacultyManage() {
        try {
            FXRouter.goTo("faculty-requests");
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onKeyPressed(KeyEvent event){
        if (event.getCode() == KeyCode.ENTER){
            onLoginButtonClick();
        }
    }
}
