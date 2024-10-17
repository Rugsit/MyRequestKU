package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.models.user.exceptions.UserException;
import ku.cs.services.FXRouter;
import ku.cs.services.SetTransition;
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
    @FXML private ImageView backgroundImageView;
    @FXML private Button loginButton;

    private UserListFileDatasource datasource;
    private int currentImageIndex = 0;
    @FXML
    public void initialize() {
        SetTransition transition = new SetTransition();
        SetTransition.setButtonBounce(loginButton);
        final String[] imagePaths = {
                getClass().getResource("/images/backgrounds/background-login1.jpg").toString(),
                getClass().getResource("/images/backgrounds/background-login2.jpg").toString(),
                getClass().getResource("/images/backgrounds/background-login3.jpg").toString(),
                getClass().getResource("/images/backgrounds/background-login4.jpg").toString(),
                getClass().getResource("/images/backgrounds/background-login5.jpg").toString(),
        };

        Image image = new Image(imagePaths[currentImageIndex]);
        backgroundImageView.setImage(image);

        SetTransition.getInstance().setSlideImageShow(backgroundImageView, imagePaths);

        datasource = new UserListFileDatasource("data", "student.csv");
        usernameTextField.requestFocus();
        hideError();
    }

    @FXML
        protected void goToLogin() {
        try {
            SetTransition.getInstance().getTimeline().stop();
            SetTransition.getInstance().setTimeline(null);
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
        String target = ",";
        int lastIndex = warningText.lastIndexOf(target);
        if (lastIndex != -1) {
            // Target substring not found
            String before = warningText.substring(0, lastIndex);
            String after = warningText.substring(lastIndex + target.length());
            warningText = before + "" + after;
        }
        // Check if user exists in the datasource
        UserList users = datasource.readData();
        User existingUser = users.findUserById(id);
        // No username in datasource
        if (existingUser == null || !existingUser.getFirstname().equalsIgnoreCase(name) ||
                !existingUser.getLastname().equalsIgnoreCase(lastName) ||
                !existingUser.getEmail().equalsIgnoreCase(email)) {
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
            } catch (UserException e) {
                showError(e.getMessage());
                System.out.println(e.getMessage());
            } catch (Exception e) {
                showError("เกิดข้อผิดพลาดกรุณาติดต่อผู้ดูแลระบบ");
                e.printStackTrace();
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
            onRegisterButtonClick();
        }
    }
}
