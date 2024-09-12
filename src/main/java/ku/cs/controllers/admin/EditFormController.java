package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ku.cs.models.user.Student;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.models.user.exceptions.UserException;
import ku.cs.services.UserListFileDatasource;

public class EditFormController {
    AdminManageStaffController currentControllAdminpage;
    UserListFileDatasource datasource;
    String currentRole;
    User currentUser;
    Stage stage;
    UserList userList;
    @FXML
    Label errorLabel;

    @FXML
    private Label facultyLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label departmentLabel;

    @FXML
    private Label advisorIdLabel;

    @FXML
    private TextField advisorIdTextField;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField userNameTextField;

    @FXML
    private TextField startPasswordTextField;

    @FXML
    private Label startPasswordLabel;

    @FXML
    private Label userNameLabel;

//--------------------------------------------------------------

//    !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//แก้ตรงนี้ไป เนื่องจาก restructure user ใหม่ check ว่าถูกมั้ย
//facultyLabel.setText(((Student)currentUser).getFaculty());
//nameLabel.setText(currentUser.getName());
//userNameLabel.setText(currentUser.getUsername());

//--------------------------------------------------------------
    public void showOldUserData(String role) {
        currentRole = role;
        facultyLabel.setText(((Student)currentUser).getFaculty());
        nameLabel.setText(currentUser.getName());
        userNameLabel.setText(currentUser.getUsername());
        if (role.equals("department-staff") || role.equals("advisor")) {
            departmentLabel.setText(((Student)currentUser).getDepartment());
        }
        if (role.equals("advisor")) {
            advisorIdLabel.setText(currentUser.getId());
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void setUserListForWrite(UserList userList) {
       this.userList = userList;
    }

    public void setCurrentControllAdminpage(AdminManageStaffController currentControllAdminpage) {
        this.currentControllAdminpage = currentControllAdminpage;
    }

    @FXML
    public void onExitClick() {
        stage.close();
    }

    @FXML
    public void onAcceptClick() {
        try {
            datasource = new UserListFileDatasource("data", currentRole + ".csv");
            currentUser.setFirstname(firstNameTextField.getText());
            currentUser.setLastname(lastNameTextField.getText());
            currentUser.setUsername(userNameTextField.getText());
            datasource.writeData(userList);
            if (currentRole.equals("department-staff")) {
                currentControllAdminpage.loadDepartmentStaff();
            } else if (currentRole.equals("faculty-staff")) {
                currentControllAdminpage.loadFacultyStaff();
            } else if (currentRole.equals("advisor")) {
                currentControllAdminpage.loadAdvisor();
            }
            stage.close();
        } catch (UserException e) {
            errorLabel.setVisible(true);
            errorLabel.setText(e.getMessage());
        }
    }
}
