package ku.cs.controllers;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import ku.cs.models.user.*;
import ku.cs.models.user.exceptions.EmailException;
import ku.cs.models.user.exceptions.PasswordException;
import ku.cs.services.*;
import ku.cs.views.components.DefaultImage;


public class UserProfileCardController{
    @FXML Label nameLabel;
    @FXML Label facultyLabel;
    @FXML Label departmentLabel;
    @FXML Label userTypeLabel;
    @FXML Label advisorLabel;
    @FXML Label idLabel;
    @FXML Label usernameLabel;
    @FXML ImageView profilePictureImageView;
    @FXML ImageView facultyIconImageView;
    @FXML ImageView departmentIconImageView;
    @FXML ImageView advisorIconImageView;
    @FXML ImageView userTypeIconImageView;
    @FXML ImageView idIconImageView;
    @FXML ImageView emailIconImageView;
    @FXML Button emailSaveButton;
    @FXML Button emailEditButton;
    @FXML Button cancelEmailEditButton;
    @FXML Button editPasswordButton;
    @FXML Button cancelEditPasswordButton;
    @FXML TextField emailTextField;
    @FXML PasswordField oldPasswordTextField;
    @FXML PasswordField newPasswordTextField;
    @FXML PasswordField confirmNewPasswordTextField;
    @FXML Label notificationLabel;
    @FXML AnchorPane anchorPane;
    private DefaultImage profilePicture;
    private User loginUser;
    private ImageDatasource imageDatasource;
    private String userRole;
    private ParentController parent;
    private String emailTextBeforeEdit;
    Datasource<UserList> datasource;
    UserList users;
    private Theme theme;

    @FXML
    public void initialize()  {
        if (loginUser == null) {return;}
        imageDatasource = new ImageDatasource("users");
        profilePicture = new DefaultImage(profilePictureImageView);
        setUpRole();
        datasource = new UserListFileDatasource("data", userRole + ".csv");
        users = datasource.readData();
        loginUser = users.findUserByUUID(loginUser.getUUID());
        showInfo();
    }

    private void setUpRole() {
        if (loginUser == null) {
            return;
        }
        userRole = loginUser.getRole();

        if (userRole.equals("student")) {return;}

        if (userRole.equals("advisor")) {
            advisorLabel.setVisible(false);
            advisorIconImageView.setVisible(false);
        }

        if (userRole.equals("department-staff") || userRole.equals("faculty-staff") || userRole.equals("admin"))  {
            advisorIconImageView.setVisible(false);
            advisorLabel.setVisible(false);
            idLabel.setVisible(false);
            idIconImageView.setVisible(false);
        }

        if (userRole.equals("faculty-staff") || userRole.equals("admin")) {
            departmentIconImageView.setVisible(false);
            departmentLabel.setVisible(false);
        }

        if (userRole.equals("admin")) {
            facultyIconImageView.setVisible(false);
            facultyLabel.setVisible(false);
        }


    }

    private void showInfo() {
        if (loginUser == null) { return; }
        nameLabel.setText(loginUser.getName());
        usernameLabel.setText(loginUser.getUsername());
        userTypeLabel.setText(loginUser.getRole());
        emailTextField.setText(loginUser.getEmail());
        onEmailSaveButtonClicked();
        profilePicture.setImage(imageDatasource.openImage(loginUser.getAvatar()));
        if (userRole.equals("faculty-staff")){
            FacultyUser facultyUser = (FacultyUser) loginUser;
            facultyLabel.setText(facultyUser.getFaculty());
        }
        else if (userRole.equals("department-staff")){
            DepartmentUser departmentUser = (DepartmentUser) loginUser;
            facultyLabel.setText(departmentUser.getFaculty());
            departmentLabel.setText(departmentUser.getDepartment());
        }
        else if (userRole.equals("advisor")){
            Advisor advisor = (Advisor) loginUser;
            facultyLabel.setText(advisor.getFaculty());
            departmentLabel.setText(advisor.getDepartment());
            idLabel.setText(advisor.getId());
        }
        else if (userRole.equals("student")){
            Student student = (Student) loginUser;
            facultyLabel.setText(student.getFaculty());
            departmentLabel.setText(student.getDepartment());
            idLabel.setText(student.getId());
            Datasource<UserList> datasource = new UserListFileDatasource("data", "advisor.csv");
            UserList advisors = datasource.readData();
            Advisor advisor = (Advisor) advisors.findUserByUUID(student.getAdvisor());
            advisorLabel.setText(advisor.getName());
        }
    }

    public void setLoginUser(User loginUser) {
        this.loginUser = loginUser;
    }

    public void setParentController(ParentController parent) {
            this.parent = parent;
    }

    @FXML
    protected void onEmailEditButtonClicked(){
        emailTextBeforeEdit = emailTextField.getText();
        emailTextField.setEditable(true);
        emailEditButton.setDisable(true);
        emailEditButton.setVisible(false);
        emailSaveButton.setDisable(false);
        emailSaveButton.setVisible(true);
        emailSaveButton.setDefaultButton(true);
        cancelEmailEditButton.setDisable(false);
        cancelEmailEditButton.setVisible(true);
        cancelEmailEditButton.setCancelButton(true);
        emailTextField.requestFocus();
        emailTextField.positionCaret(emailTextField.getText().length());
        emailTextField.setStyle("-fx-border-color: #b3b1b1; -fx-border-width: 1px");
    }
    @FXML
    protected void onEmailSaveButtonClicked(){
        emailTextField.setEditable(false);
        emailEditButton.setDisable(false);
        emailEditButton.setVisible(true);
        emailSaveButton.setDisable(true);
        emailSaveButton.setVisible(false);
        emailSaveButton.setDefaultButton(false);
        cancelEmailEditButton.setDisable(true);
        cancelEmailEditButton.setVisible(false);
        cancelEmailEditButton.setCancelButton(false);
        emailTextField.setStyle("-fx-border-width: 0px");
    }

    @FXML
    protected void onCancelEmailEditButtonClicked(){
        emailTextField.setText(emailTextBeforeEdit);
        onEmailSaveButtonClicked();
    }

    @FXML
    protected void onUploadImageButtonClicked(){
        imageDatasource = new ImageDatasource("users");
        if (!imageDatasource.uploadImage(loginUser.getDefaultAvatarName()).equals("no-image")) {
            profilePicture.setImage(imageDatasource.openImage("tmp"));
        }
    }

    @FXML
    protected void onSaveButtonClicked() {
        if (emailTextField.isEditable()) {
            showNotification("โปรดแก้ไขอีเมลให้เสร็จก่อนกดบันทึกข้อมูล", true);
            return;
        }
        try {
            String changedData = "";
            boolean isChanged = false;
            if (!loginUser.getEmail().equals(emailTextField.getText())) {
                changedData += "ข้อมูลอีเมล";
                isChanged = true;
            }
            loginUser.setEmail(emailTextField.getText());
            String savedImageName = imageDatasource.saveImage();
            if ( savedImageName != null && !savedImageName.equals("no-image") ) {
                if (isChanged) changedData += " และ";
                changedData += "รูปโปรไฟล์";
                isChanged = true;
                loginUser.setAvatar(savedImageName);
            }
            if (isChanged) {
                showNotification("บันทึก" + changedData + "สำเร็จ!", false);
                datasource.writeData(users);
                if (parent != null) {
                    parent.setLoginUser(loginUser);
                    parent.loadProfile();
                }
            }
        } catch (EmailException e) {
            showNotification(e.getMessage(), true);
        }
    }

    private void showNotification(String messages, boolean error) {
        if (error) {
            if (Theme.getInstance().getCurrentTheme().equals("light")) {
                notificationLabel.setStyle("-fx-text-fill: red;");
            } else {
                notificationLabel.setStyle("-fx-text-fill: #ff908a;");
            }
        } else {
            if (Theme.getInstance().getCurrentTheme().equals("light")) {
                notificationLabel.setStyle("-fx-text-fill: green;");
            } else {
                notificationLabel.setStyle("-fx-text-fill: #9effb8;");
            }

        }
        notificationLabel.setText(messages);
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setNode(notificationLabel);
        fadeTransition.setDuration(Duration.seconds(1.75));
        fadeTransition.setCycleCount(2);
        fadeTransition.setInterpolator(Interpolator.LINEAR);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.setAutoReverse(true);
        fadeTransition.play();
    }

    private void changeEditPasswordButton(String text, String color) {
        editPasswordButton.setText(text);
        editPasswordButton.setStyle(
                "    -fx-background-color: "+ color + ";" +
                        "-fx-cursor: hand;" +
                        "-fx-text-fill: #FFFFFF;" +
                        "-fx-font-weight: bold;" +
                        "-fx-pref-height: 30;" +
                        "-fx-background-radius: 10;"
        );
        editPasswordButton.getStyleClass().add(".semi-medium-font-size");
    }

    @FXML
    protected void onEditPasswordButtonClicked() {
        if (!oldPasswordTextField.isVisible() && !newPasswordTextField.isVisible()) {
            oldPasswordTextField.setDisable(false);
            oldPasswordTextField.setEditable(true);
            oldPasswordTextField.setVisible(true);
            oldPasswordTextField.setStyle("-fx-border-color: #b3b1b1;");
            changeEditPasswordButton("ยืนยันรหัสผ่านเก่า", "#2c6dac");
            editPasswordButton.setDefaultButton(true);
            cancelEditPasswordButton.setDisable(false);
            cancelEditPasswordButton.setVisible(true);

        } else if (!oldPasswordTextField.isDisabled()) {
            String oldPassword = oldPasswordTextField.getText();
            try {
                boolean correctPassword = loginUser.validatePassword(oldPassword);
                if (!correctPassword) {
                    showNotification("รหัสผ่านไม่ถูกต้อง", true);
                } else {
                    oldPasswordTextField.setDisable(true);
                    oldPasswordTextField.setEditable(false);
                    oldPasswordTextField.setVisible(false);
                    oldPasswordTextField.setStyle("-fx-border-color: white;");
                    oldPasswordTextField.clear();
                    newPasswordTextField.setDisable(false);
                    newPasswordTextField.setVisible(true);
                    newPasswordTextField.setEditable(true);
                    newPasswordTextField.setStyle("-fx-border-color: #b3b1b1;");
                    confirmNewPasswordTextField.setDisable(false);
                    confirmNewPasswordTextField.setVisible(true);
                    confirmNewPasswordTextField.setEditable(true);
                    confirmNewPasswordTextField.setStyle("-fx-border-color: #b3b1b1;");
                    changeEditPasswordButton("ยืนยันการเปลี่ยนรหัส", "#5dbc56");
                }
            } catch (PasswordException e) {
                showNotification(e.getMessage(), true);
            }
        } else if(!newPasswordTextField.isDisabled()) {
            String newPassword = newPasswordTextField.getText();
            String confirmPassword = confirmNewPasswordTextField.getText();
            if (!newPassword.equals(confirmPassword)) {
                showNotification("โปรดกรอกยืนยันรหัสผ่านให้ตรงกับรหัสผ่านใหม่", true);
            }
            else {
                try {
                    loginUser.setPassword(newPassword);
                    onCancelEditPasswordButtonClicked();
                    showNotification("เปลี่ยนรหัสผ่านสำเร็จ!", false);
                    datasource.writeData(users);
                } catch (PasswordException e) {
                    showNotification(e.getMessage(), true);
                }
            }
        }
    }

    @FXML
    protected void onCancelEditPasswordButtonClicked() {
        changeEditPasswordButton("แก้ไขรหัสผ่าน", "#FF4E4E");
        oldPasswordTextField.setDisable(true);
        oldPasswordTextField.setEditable(false);
        oldPasswordTextField.setVisible(false);
        oldPasswordTextField.setStyle("-fx-border-color: white;");
        oldPasswordTextField.clear();
        newPasswordTextField.setDisable(true);
        newPasswordTextField.setVisible(false);
        newPasswordTextField.setEditable(false);
        newPasswordTextField.setStyle("-fx-border-color: white;");
        newPasswordTextField.clear();
        confirmNewPasswordTextField.setDisable(true);
        confirmNewPasswordTextField.setVisible(false);
        confirmNewPasswordTextField.setEditable(false);
        confirmNewPasswordTextField.setStyle("-fx-border-color: white;");
        confirmNewPasswordTextField.clear();
        editPasswordButton.setDefaultButton(false);
        cancelEditPasswordButton.setDisable(true);
        cancelEditPasswordButton.setVisible(false);
    }
}
