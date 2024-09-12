package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.services.Authentication;
import ku.cs.services.FXRouter;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;

public class RegisterController {
    @FXML private TextField usernameTextField;
    @FXML private TextField passwordTextField;
    @FXML private TextField confirmPasswordTextField;
    @FXML private TextField nameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private TextField studentIdTextField;
    @FXML private TextField emailTextField;
    @FXML private Label errorLabel;

    private UserListFileDatasource datasource;
    private Authentication authentication;
    private User user;
    @FXML
    public void initialize() {
        datasource = new UserListFileDatasource("data", "student.csv");
        usernameTextField.requestFocus();
        hideError();
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

    private String getTextFieldInput(TextField fieldName){
        return fieldName.getText().trim();
    }

    @FXML
    protected void onRegisterButtonClick() {
        String username = getTextFieldInput(usernameTextField);
        String password = getTextFieldInput(passwordTextField);
        String confirmPassword = getTextFieldInput(confirmPasswordTextField);
        String name = getTextFieldInput(nameTextField);
        String lastName = getTextFieldInput(lastNameTextField);
        String id = getTextFieldInput(studentIdTextField);
        String email = getTextFieldInput(emailTextField);

        // Basic warning
        hideError();
        String warningText = "กรุณากรอก";
        if (username.isEmpty()) warningText += "ชื่อผู้ใช้, ";
        if (password.isEmpty() || confirmPassword.isEmpty()) warningText += "รหัสผ่าน, ";
        if (name.isEmpty()) warningText += "ชื่อ, ";
        if (lastName.isEmpty()) warningText += "นามสกุล, ";
        if (id.isEmpty()) warningText += "รหัสนิสิต, ";
        if (email.isEmpty()) warningText += "อีเมลล์, ";

        // Check if user exists in the datasource
        UserList users = datasource.readData();
        User existingUser = users.findUserById(id);
        // No username in datasource
        if (existingUser == null || !existingUser.getFirstname().equalsIgnoreCase(name) ||
                !existingUser.getLastname().equalsIgnoreCase(lastName)) {
            showError("ไม่มีชื่อผู้ใช้งานในระบบ");
        }
        // Display basic warning.
        else if (!warningText.equals("กรุณากรอก")) {
            if (password.equals(confirmPassword)) {
                showError(warningText);
            } else if (!password.equals(confirmPassword) && !password.isEmpty() && !confirmPassword.isEmpty()) {
                showError("รหัสผ่านต้องตรงกันทั้งคู่");
            }
        } else {
            // Pass user into a User class.
            hideError();
            try {
                if (existingUser.getUsername().equals("no-username")) {
                    existingUser.setActive(true);
                    existingUser.setEmail(email);
                    existingUser.setUsername(username);
                    existingUser.setPassword(password);
                    datasource.writeData(users);
                    goToLogin();
                } else {
                    showError("ผู้ใช้งานได้ทำการลงทะเบียนก่อนหน้าเรียบร้อยแล้ว");
                }
            } catch (Exception e) {
                showError("กรุณากรอกรหัสผ่านที่มีความยาวมากกว่า 8 ตัวอักษร");
                System.out.println(e.getMessage());
            }
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
    protected void onKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER){
            System.out.println("register");
        }
    }
}
