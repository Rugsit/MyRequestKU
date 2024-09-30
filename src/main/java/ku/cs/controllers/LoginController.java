package ku.cs.controllers;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.transform.Translate;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import ku.cs.models.Session;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.models.user.exceptions.DateException;
import ku.cs.services.Authentication;
import ku.cs.services.SetTransition;
import ku.cs.services.UserListFileDatasource;
import ku.cs.views.components.DefaultLabel;
import ku.cs.services.FXRouter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.Stack;

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
    @FXML private Stage currentPopupStage;
    @FXML private ImageView backgroundImageView;
    @FXML private ImageView nextBackgroundImageView;
    @FXML private Button loginButton;

    private Authentication authController;
    private UserListFileDatasource datasource;
    private User loginUser = null;
    private int currentImageIndex = 0;

    @FXML
    private void initialize() {
        SetTransition transition = new SetTransition();
        transition.setButtonBounce(loginButton);
        final String[] imagePaths = {
                getClass().getResource("/images/backgrounds/background-login1.jpg").toString(),
                getClass().getResource("/images/backgrounds/background-login2.jpg").toString(),
                getClass().getResource("/images/backgrounds/background-login3.jpg").toString(),
                getClass().getResource("/images/backgrounds/background-login4.jpg").toString(),
                getClass().getResource("/images/backgrounds/background-login5.jpg").toString(),
        };

        Image image = new Image(imagePaths[currentImageIndex]);
        backgroundImageView.setImage(image);

        transition.setSlideImageShow(backgroundImageView, imagePaths);

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

                // change default password check.
                if (loginUser.getDefaultPassword().equals(password) && !loginUser.getRole().equals("student")){
                    System.out.println("Need to change password");
                    changePasswordPopup(loginUser);
                    userNameTextField.setText("");
                    passwordTextField.setText("");
                }
                else{
                    if (loginUser.getRole().equalsIgnoreCase("faculty-staff")) {goToFacultyManage();}
                    else if (loginUser.getRole().equalsIgnoreCase("admin")) {goToAdminManage();}
                    else if (loginUser.getRole().equalsIgnoreCase("student")){onStudentButtonClicked();}
                    else if (loginUser.getRole().equalsIgnoreCase("advisor")){goToAdvisorManage();}
                    else if (loginUser.getRole().equalsIgnoreCase("department-staff")){goToDepartmentManage();}
                }


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

    private void changePasswordPopup(User currentUser) {
        try {
            if (currentPopupStage == null || !currentPopupStage.isShowing()) {
                currentPopupStage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/change-password.fxml"));
                Scene scene = new Scene(fxmlLoader.load());

                ChangePasswordController controller = fxmlLoader.getController();
                controller.setCurrentUser(currentUser);
                controller.setStage(currentPopupStage);

                currentPopupStage.setScene(scene);
                currentPopupStage.initModality(Modality.APPLICATION_MODAL);
                currentPopupStage.setTitle("Change password");
                currentPopupStage.show();
            }
        } catch (IOException e) {
            System.out.println("Error :" + e.getMessage());
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
            FXRouter.goTo("admin-user-profile", loginUser);
        } catch (IOException e) {
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
            Session session = new Session();
            if(loginUser != null){
                session.setUser(loginUser);
            }
            FXRouter.goTo("department-staff-request-list",session);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void goToFacultyManage() {
        try {
            FXRouter.goTo("faculty-page", loginUser);
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
