package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import ku.cs.controllers.student.StudentRequestsController;
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
    private BorderPane borderPane;
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

    }

    public void showDataOnCard() {
        nameLabel.setText(loginUser.getName());
        userNameLabel.setText(loginUser.getUsername());
        dafaultPasswordLabel.setText(loginUser.getDefaultPassword());
    }

    public void setLoginUser(Admin loginUser) {
        this.loginUser = loginUser;
    }
}
