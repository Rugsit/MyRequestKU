package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import ku.cs.models.user.Admin;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

import javafx.scene.control.Label;
import ku.cs.services.UserListFileDatasource;

public class AdminProfileController {
    private Datasource<UserList> datasource;

    private Admin loginUser;

    private UserList userList;


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
        datasource = new UserListFileDatasource("data", "admin.csv");
        userList = ((UserListFileDatasource)datasource).readAllUser();
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
            HashMap<String, Object> pack = new HashMap<>();
            pack.put("loginUser", loginUser);
            pack.put("userList", userList);
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
