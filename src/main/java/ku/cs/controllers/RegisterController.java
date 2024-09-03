package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ku.cs.models.user.User;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.UserListFileDatasource;
import org.w3c.dom.Text;

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
    private AuthenticationController authController;
    private User user;
    @FXML
    public void initialize() {
        datasource = new UserListFileDatasource("data", "student.csv");
        authController = new AuthenticationController();
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
    protected void onRegisterButtonClick(){
        String username = getTextFieldInput(usernameTextField);
        String password = getTextFieldInput(passwordTextField);
        String confirmPassword = getTextFieldInput(confirmPasswordTextField);
        String name = getTextFieldInput(nameTextField);
        String lastName = getTextFieldInput(lastNameTextField);
        String id = getTextFieldInput(studentIdTextField);
        String email = getTextFieldInput(emailTextField);

        // basic warning
        hideError();
        String warningText = "กรุณากรอก";
        if (username.isEmpty()) {warningText += "ชื่อผู้ใช้, ";}
        if (password.isEmpty() || confirmPassword.isEmpty()) {warningText += "รหัสผ่าน, ";}
        if (name.isEmpty()){warningText += "ชื่อ, ";}
        if (lastName.isEmpty()) {warningText += "นามสกุล, ";}
        if (id.isEmpty()) {warningText += "รหัสนิสิต, ";}
        if (email.isEmpty()){warningText += "อีเมลล์, ";}

        // No username in datasource
        if (!authController.isUserInDatasource(username)) {
            showError("ไม่มีชื่อผู้ใช้งานในระบบ");}
            // display basic warning.
        else if (!warningText.equals("กรุณากรอก")){
            if (password.equals(confirmPassword)) {showError(warningText);}
            else if (!password.equals(confirmPassword) && !password.isEmpty() && !confirmPassword.isEmpty()) {showError("รหัสผ่านต้องตรงกันทั้งคู่");}
            }
        else {
            // Pass user into a User class.
            hideError();
            try{
                user = new User(id, username, "student", name, lastName, "2004-09-26:00:00:00:+0000", email, "none", "none", password);
                datasource.appendData(user);
                goToLogin(); // go to login page if successful add register user and append to datasource.
            } catch (Exception e){
                showError("กรุณากรอกรหัสผ่านที่มีความยาวมากกว่า 9 ตัวอักษร");
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
