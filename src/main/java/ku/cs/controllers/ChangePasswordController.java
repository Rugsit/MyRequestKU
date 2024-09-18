package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.services.UserListFileDatasource;

import java.util.Stack;

public class ChangePasswordController {
    @FXML
    private Button acceptButton;
    @FXML
    private Button exitButton;
    @FXML
    private Label displayTextField;

    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField confirmPasswordTextField;
    @FXML
    private Label errorLabel;

    private Stage stage;
    private User currentUser;
    private UserListFileDatasource datasource;

    public void setStage(Stage stage){this.stage = stage;}
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        displayTextField.setText("เปลี่ยนรหัสผ่าน ( " + currentUser.getUsername() + " )");}

    private boolean passwordChange() {
        String password1 = passwordTextField.getText().trim();
        String password2 = confirmPasswordTextField.getText().trim();

        if (password1.equals(password2)) {
            errorLabel.setVisible(false);
            return true;
        } else {
            errorLabel.setVisible(true);
            return false;
        }
    }


    @FXML
    private void onAcceptClick() {
        // change password and save to datasource.
        if (passwordChange()){
            try{ // writing user data.
                String fileName = currentUser.getRole();

                datasource = new UserListFileDatasource("data", fileName+".csv");
                UserList users = datasource.readData();
                User existingUser = users.findUserById(currentUser.getId());
                existingUser.setPassword(passwordTextField.getText().trim());
                datasource.writeData(users);

                stage.close();

            }catch (Exception e){
                errorLabel.setVisible(true);
                errorLabel.setText("กรุณากรอกรหัสผ่านที่มีความยาวมากกว่า 8 ตัวอักษร");
                System.out.println(e.getMessage());
            }


        }
    }

    @FXML
    private void onExitClick() {
        System.out.println("Exit button clicked");
        if (stage != null) {
            stage.close();
        }
    }

}
