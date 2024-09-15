package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.models.user.exceptions.DateException;
import ku.cs.services.Authentication;
import ku.cs.services.UserListFileDatasource;
import ku.cs.views.components.DefaultLabel;
import ku.cs.services.FXRouter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    private Authentication authController;
    private UserListFileDatasource datasource;
    private User loginUser = null;

    @FXML
    private void initialize() {
        errorLabel.setText("");
        DefaultLabel aboutUs = new DefaultLabel(aboutUsLabel);
        authController = new Authentication();
    }
    @FXML
    protected void onLoginButtonClick(){
        String username = userNameTextField.getText().trim();
        String password = passwordTextField.getText().trim();
        User isUseridInDatasource = authController.isUserInDatasource(username);
        try {
            loginUser = authController.loginAuthenticate(username, password);
        } catch (Exception e) {
            errorLabel.setText("ชื่อผู้ใช้ หรือรหัสผ่านไม่ถูกต้อง");
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
            if (!username.isEmpty() && !password.isEmpty() && loginUser.getActiveStatus().equals("Inactive")){
                showError("บัญชี้นี้ได้ถูกระงับการใช้งานชั่วคราว");
            }
            else{
                try {
                    String fileName = loginUser.getRole();
                    datasource = new UserListFileDatasource("data", fileName+".csv");
                    UserList users = datasource.readData();
                    User existingUser = users.findUserById(loginUser.getId());
                    existingUser.setLastLogin(LocalDateTime.now().format(DateTimeFormatter.ofPattern(User.DATE_FORMAT)));
                    datasource.writeData(users);
                } catch (Exception e){
                    showError("ไม่สามารถบันทึกเวลาในการเข้าใช้ได้");
                }

                hideError();
                if (loginUser.getRole().equalsIgnoreCase("faculty-staff")) {goToFacultyManage();}
                else if (loginUser.getRole().equalsIgnoreCase("admin")) {goToAdminManage();}
                else if (loginUser.getRole().equalsIgnoreCase("student")){onStudentButtonClicked();}
                else if (loginUser.getRole().equalsIgnoreCase("advisor")){goToAdvisorManage();}
                else if (loginUser.getRole().equalsIgnoreCase("department-staff")){goToDepartmentManage();}
            }
        } else if (!username.isEmpty() && !password.isEmpty() && isUseridInDatasource == null) {
            showError("ชื่อผู้ใช้ หรือรหัสผ่านไม่ถูกต้อง");
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
            FXRouter.goTo("student-page", loginUser);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void goToAdvisorManage() {
        try {
            FXRouter.goTo("advisor-students", loginUser);
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
