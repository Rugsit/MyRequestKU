package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ku.cs.models.department.Department;
import ku.cs.models.department.DepartmentList;
import ku.cs.models.faculty.Faculty;
import ku.cs.models.faculty.FacultyList;
import ku.cs.models.user.*;
import ku.cs.models.user.exceptions.UserException;
import ku.cs.services.DepartmentListFileDatasource;
import ku.cs.services.FacultyListFileDatasource;
import ku.cs.services.UserListFileDatasource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class AddFormController {
    AdminManageStaffController currentControllAdminpage;
    String currentRole;
    Stage stage;
    UserList userList;
    @FXML
    Label errorLabel;

    @FXML
    ChoiceBox<String> departmentChoiceBox;
    @FXML
    ChoiceBox<String> facultyChoiceBox;

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
    private TextField startPassword;

    @FXML
    private Label startPasswordLabel;

    @FXML
    private Label userNameLabel;


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setRole(String role) {
        currentRole = role;
    }
    public void setUserListForWrite(UserList userList) {
       this.userList = userList;
    }

    public void setCurrentControllAdminpage(AdminManageStaffController currentControllAdminpage) {
        this.currentControllAdminpage = currentControllAdminpage;
    }

    public void setChoiceBox() {
        FacultyListFileDatasource datasourceFaculty = new FacultyListFileDatasource("data");
        DepartmentListFileDatasource datasourceDepartment = new DepartmentListFileDatasource("data");
        FacultyList list =  datasourceFaculty.readData();
        DepartmentList departmentList = datasourceDepartment.readData();
        for (Faculty faculty : list.getFacultyList()) {
            facultyChoiceBox.getItems().add(faculty.getName());
        }
        if (departmentChoiceBox != null) {
            for (Department department : departmentList.getDepartments()) {
                departmentChoiceBox.getItems().add(department.getName());
            }
        }
    }

    @FXML
    public void onExitClick() {
        stage.close();
    }

    @FXML
    public void onAcceptClick() {
        UUID uuid = UUID.randomUUID();
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH:mm:ss");
        UserListFileDatasource datasource = new UserListFileDatasource("data", currentRole+".csv");
        try {
            if (facultyChoiceBox.getValue() == null) throw new UserException("กรุณาเลือกคณะ");
            if (departmentChoiceBox != null && departmentChoiceBox.getValue() == null) throw new UserException("กรุณาเลือกภาควิชา");
            if (currentRole.equals("faculty-staff")) {
                FacultyUser facultyUser = new FacultyUser(uuid.toString(), "0000000000", userNameTextField.getText(), "faculty-staff", firstNameTextField.getText(), lastNameTextField.getText(), date.format(formatter), "fscixxa@ku.th", startPassword.getText(), "no-image", "active", facultyChoiceBox.getValue());
                userList.addUser(facultyUser);
                datasource.writeData(userList.getFacultyList());
                currentControllAdminpage.loadFacultyStaff();
            } else if (currentRole.equals("department-staff")) {
                DepartmentUser departmentUser = new DepartmentUser(uuid.toString(), "0000000000", userNameTextField.getText(), "department-staff", firstNameTextField.getText(), lastNameTextField.getText(), date.format(formatter), "fscixxa@ku.th", startPassword.getText(), "no-image", "active", facultyChoiceBox.getValue(), departmentChoiceBox.getValue());
                userList.addUser(departmentUser);
                datasource.writeData(userList.getDepartmentList());
                currentControllAdminpage.loadDepartmentStaff();
            } else if (currentRole.equals("advisor")) {
                Advisor advisor = new Advisor(uuid.toString(), advisorIdTextField.getText(), userNameTextField.getText(), "advisor", firstNameTextField.getText(), lastNameTextField.getText(), date.format(formatter), "fscixxa@ku.th", startPassword.getText(), "no-image", "active", facultyChoiceBox.getValue(), departmentChoiceBox.getValue());
                userList.addUser(advisor);
                datasource.writeData(userList.getAdvisorList());
                currentControllAdminpage.loadAdvisor();
            }
            stage.close();
        } catch (UserException e) {
            errorLabel.setVisible(true);
            errorLabel.setText(e.getMessage());
        }
    }


}
