package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import ku.cs.models.user.Admin;
import javafx.scene.control.Label;

public class AdminProfileController {

    private Admin loginUser;

    @FXML
    private Label nameLabel;
    @FXML
    private Label userNameLabel;
    @FXML
    private Label dafaultPasswordLabel;

    public void showDataOnCard() {
        nameLabel.setText(loginUser.getName());
        userNameLabel.setText(loginUser.getUsername());
        dafaultPasswordLabel.setText(loginUser.getDefaultPassword());
    }

    public void setLoginUser(Admin loginUser) {
        this.loginUser = loginUser;
    }
}
