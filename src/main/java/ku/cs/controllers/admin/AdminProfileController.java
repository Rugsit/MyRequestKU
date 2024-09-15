package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import ku.cs.models.user.Admin;
import ku.cs.services.FXRouter;

import java.awt.*;
import java.io.IOException;
import javafx.scene.control.Label;

public class AdminProfileController {
    Admin loginUser;


    @FXML
    private Label nameLabel;
    @FXML
    private Label userNameLabel;
    @FXML
    private Label dafaultPasswordLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private ImageView image;

    @FXML
    public void initialize() {
        if (FXRouter.getData() instanceof Admin) {
            loginUser = (Admin) FXRouter.getData();
        }
        showDataOnCard();
    }

    private void showDataOnCard() {
        nameLabel.setText(loginUser.getName());
        userNameLabel.setText(loginUser.getUsername());
        dafaultPasswordLabel.setText(loginUser.getDefaultPassword());
    }

    @FXML
    protected void goToAdminManageStaff() {
        try {
            FXRouter.goTo("admin-manage-staff", loginUser);
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void goToAdminManageFaculty() {
        try {
            FXRouter.goTo("admin-manage-faculty-department", loginUser);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void goToAdminManageUsers() {
        try {
            FXRouter.goTo("admin-manage-users", loginUser);
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onLogoutClicked() {
        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
