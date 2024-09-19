package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import ku.cs.models.department.Department;
import ku.cs.models.department.DepartmentList;
import ku.cs.models.faculty.Faculty;
import ku.cs.models.faculty.FacultyList;
import ku.cs.models.user.*;
import ku.cs.models.user.exceptions.UserException;
import ku.cs.services.Datasource;
import ku.cs.services.DepartmentListFileDatasource;
import ku.cs.services.FacultyListFileDatasource;
import ku.cs.services.UserListFileDatasource;
import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class AddFormController {
    // store controller ref for user their method example. reload main page when edit data
    private AdminManageStaffController adminStaffController;
    private AdminManageFacultyController adminFacultyController;

    // store temp data for interact with behavior in scene
    private String prevFacaltyChose;
    private String currentRole;


    // dataList for write data to file csv
    private FacultyList facultyList;
    private DepartmentList departmentList;
    private UserList userList;

    // JavaFX component
    @FXML
    private Stage stage;
    @FXML
    private TextField facultyNameTextField;
    @FXML
    private TextField facultyIdTextField;
    @FXML
    private TextField departmentNameTextField;
    @FXML
    private TextField departmentIdTextField;
    @FXML
    private Label errorLabel;
    @FXML
    private ChoiceBox<String> departmentChoiceBox;
    @FXML
    private ChoiceBox<String> facultyChoiceBox;
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

    @FXML
    private void initialize() {
        DepartmentListFileDatasource datasourceDepartment = new DepartmentListFileDatasource("data");
        departmentList = datasourceDepartment.readData();
        if (facultyChoiceBox != null && departmentChoiceBox != null) {
            facultyChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    departmentChoiceBox.setValue("");
                }
            });
        }
        if (departmentChoiceBox != null) {
            departmentChoiceBox.setOnMouseClicked(e -> {
                showDepartmentInChoiceBox();
            });
            departmentChoiceBox.setOnKeyPressed(e -> {
                showDepartmentInChoiceBox();
            });
        }
    }

    private void showDepartmentInChoiceBox() {
        if (facultyChoiceBox.getValue() != null && prevFacaltyChose != facultyChoiceBox.getValue()) {
            prevFacaltyChose = facultyChoiceBox.getValue();
            departmentChoiceBox.getItems().clear();

            for (Department department : departmentList.getDepartments()) {
                if (department.getFaculty().equals(facultyChoiceBox.getValue())) {
                    departmentChoiceBox.getItems().add(department.getName());
                }
            }
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setRole(String role) {
        currentRole = role;
    }
    public void setListForWrite(Object objectlist) {
        if (objectlist instanceof DepartmentList) {
            this.departmentList = (DepartmentList) objectlist;
        } else if (objectlist instanceof FacultyList) {
            this.facultyList = (FacultyList) objectlist;
        } else if (objectlist instanceof UserList) {
            this.userList = (UserList) objectlist;
        }
    }

    public void setCurrentControllpage(Object object) {
        if (object instanceof AdminManageStaffController) {
            this.adminStaffController = (AdminManageStaffController) object;
        } else if (object instanceof AdminManageFacultyController) {
            this.adminFacultyController = (AdminManageFacultyController) object;
        }
    }

    public void setChoiceBox() {
        if (facultyChoiceBox != null) {
            FacultyListFileDatasource datasourceFaculty = new FacultyListFileDatasource("data");
            FacultyList list =  datasourceFaculty.readData();
            for (Faculty faculty : list.getFacultyList()) {
                facultyChoiceBox.getItems().add(faculty.getName());
            }
        }
        if (departmentChoiceBox != null) {
            DepartmentListFileDatasource datasourceDepartment = new DepartmentListFileDatasource("data");
            DepartmentList departmentList = datasourceDepartment.readData();
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
                FacultyUser facultyUser = new FacultyUser("0000000000", userNameTextField.getText(), "faculty-staff", firstNameTextField.getText(), lastNameTextField.getText(), date.format(formatter), "fscixxa@ku.th", startPassword.getText(), facultyChoiceBox.getValue());
                userList.addUser(facultyUser);
                datasource.writeData(userList.getUserList(currentRole));
                adminStaffController.loadFacultyStaff();
            } else if (currentRole.equals("department-staff")) {
                DepartmentUser departmentUser = new DepartmentUser("0000000000", userNameTextField.getText(), "department-staff", firstNameTextField.getText(), lastNameTextField.getText(), date.format(formatter), "fscixxa@ku.th", startPassword.getText(), facultyChoiceBox.getValue(), departmentChoiceBox.getValue());
                userList.addUser(departmentUser);
                datasource.writeData(userList.getUserList(currentRole));
                adminStaffController.loadDepartmentStaff();
            } else if (currentRole.equals("advisor")) {
                Advisor advisor = new Advisor(advisorIdTextField.getText(), userNameTextField.getText(), "advisor", firstNameTextField.getText(), lastNameTextField.getText(), date.format(formatter), "fscixxa@ku.th", startPassword.getText(), facultyChoiceBox.getValue(), departmentChoiceBox.getValue());
                userList.addUser(advisor);
                datasource.writeData(userList.getUserList(currentRole));
                adminStaffController.loadAdvisor();
            }
            adminStaffController.resetSearch();
            stage.close();
        } catch (UserException e) {
            errorLabel.setVisible(true);
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void onAcceptFacultyDepartmentClick() {
        try {
            if (departmentList != null && departmentNameTextField != null) {
                Datasource<DepartmentList> datasource = new DepartmentListFileDatasource("data");
                departmentList.addDepartment(departmentNameTextField.getText(), departmentIdTextField.getText(), facultyChoiceBox.getValue());
                datasource.writeData(departmentList);
                adminFacultyController.loadDepartment();
            } else if (facultyList != null && facultyNameTextField != null) {
                Datasource<FacultyList> datasource = new FacultyListFileDatasource("data");
                facultyList.addFaculty(facultyNameTextField.getText(), facultyIdTextField.getText());
                datasource.writeData(facultyList);
                adminFacultyController.loadFaculty();
            }
            adminFacultyController.resetSearch();
            stage.close();
        } catch (IllegalArgumentException e) {
            errorLabel.setVisible(true);
            errorLabel.setText(e.getMessage());
        }
    }


}
