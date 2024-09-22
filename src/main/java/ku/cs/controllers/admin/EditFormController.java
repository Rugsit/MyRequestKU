package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ku.cs.models.department.Department;
import ku.cs.models.department.DepartmentList;
import ku.cs.models.department.NoFacultyException;
import ku.cs.models.faculty.Faculty;
import ku.cs.models.faculty.FacultyList;
import ku.cs.models.user.*;
import ku.cs.models.user.exceptions.UserException;
import ku.cs.services.Datasource;
import ku.cs.services.DepartmentListFileDatasource;
import ku.cs.services.FacultyListFileDatasource;
import ku.cs.services.UserListFileDatasource;
import org.w3c.dom.Text;

import java.util.Set;
import java.util.stream.Collectors;

public class EditFormController {
    // store controller ref for user their method example. reload main page when edit data
    private AdminManageStaffController currentControllStaffPage;
    private AdminManageFacultyController currentControllFacultyPage;

    // store temp data for interact with behavior in scene
    private String prevFacaltyChose;
    private String currentRole;

    // dataList for write data to file and model class that store what current data is select for edit
    private Faculty faculty;
    private FacultyList facultyList;
    private Department department;
    private DepartmentList departmentList;
    private User currentUser;
    private UserList userList;

    // FXML Component
    @FXML
    private Stage stage;
    @FXML
    private Label departmentNameLabel;
    @FXML
    private TextField facultyTextField;
    @FXML
    private TextField facultyNameTextField;
    @FXML
    private TextField facultyIdTextField;
    @FXML
    private Label facultyNameLabel;
    @FXML
    private Label facultyIdLabel;
    @FXML
    private TextField departmentIdTextField;
    @FXML
    private Label departmentIdLabel;
    @FXML
    private ChoiceBox<String> departmentChoiceBox;
    @FXML
    private ChoiceBox<String> facultyChoiceBox;
    @FXML
    private Label errorLabel;
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
    private TextField departmentNameTextField;
    @FXML
    private Label startPasswordLabel;
    @FXML
    private Label userNameLabel;
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private void initialize() {
        Datasource<DepartmentList> datasource = new DepartmentListFileDatasource("data");
        departmentList = datasource.readData();
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

        anchorPane.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                onAcceptClick();
            }
        });
    }

    private void showDepartmentInChoiceBox() {
        if (facultyChoiceBox.getValue() != null && prevFacaltyChose != facultyChoiceBox.getValue()) {
            prevFacaltyChose = facultyChoiceBox.getValue();
            departmentChoiceBox.getItems().clear();
            Set<String> filter = departmentList.getDepartments()
                    .stream()
                    .filter(department -> department.getFaculty().equals(facultyChoiceBox.getValue()))
                    .map(Department::getName)
                    .collect(Collectors.toSet());

            departmentChoiceBox.getItems().addAll(filter);
        }
    }

    public void showOldFacultyDepartmentData() {
        if (faculty == null) {
            facultyNameLabel.setText(department.getFaculty());
            facultyChoiceBox.setValue(department.getFaculty());
            departmentNameLabel.setText(department.getName());
            departmentNameTextField.setText(department.getName());
            departmentIdLabel.setText(department.getId());
            departmentIdTextField.setText(department.getId());
        } else if (department == null) {
            facultyNameLabel.setText(faculty.getName());
            facultyNameTextField.setText(faculty.getName());
            facultyIdLabel.setText(faculty.getId());
            facultyIdTextField.setText(faculty.getId());
        }
    }

    public void showOldUserData(String role) {
        currentRole = role;
        if (currentUser instanceof FacultyUser) {
            facultyLabel.setText(((FacultyUser)currentUser).getFaculty());
            facultyChoiceBox.setValue(((FacultyUser)currentUser).getFaculty());
        }
        nameLabel.setText(currentUser.getName());
        firstNameTextField.setText(currentUser.getFirstname());
        lastNameTextField.setText(currentUser.getLastname());
        userNameLabel.setText(currentUser.getUsername());
        userNameTextField.setText(currentUser.getUsername());
        startPasswordTextField.setText("DEFAULT");
        startPasswordLabel.setText(currentUser.getDefaultPassword());
        if (currentUser instanceof DepartmentUser) {
            departmentLabel.setText(((DepartmentUser)currentUser).getDepartment());
            departmentChoiceBox.setValue(((DepartmentUser)currentUser).getDepartment());
        }
        if (currentUser instanceof Advisor) {
            advisorIdLabel.setText(currentUser.getId());
            advisorIdTextField.setText(currentUser.getId());
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


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCurrentObject(Object object) {
        if (object instanceof Department) {
            department = (Department) object;
        } else if (object instanceof Faculty) {
            faculty = (Faculty) object;
        } else if (object instanceof User) {
            this.currentUser = (User) object;
        }
    }

    public void setListForWrite(Object object) {
        if (object instanceof DepartmentList) {
           departmentList = (DepartmentList) object;
        } else if (object instanceof FacultyList) {
            facultyList = (FacultyList) object;
        } else if (object instanceof UserList) {
            this.userList = (UserList) object;
        }
    }

    public void setCurrentControllPage(Object object) {
       if (object instanceof AdminManageStaffController) {
           this.currentControllStaffPage = (AdminManageStaffController) object;
       } else if (object instanceof AdminManageFacultyController) {
           this.currentControllFacultyPage = (AdminManageFacultyController) object;
       }
    }

    @FXML
    public void onExitClick() {
        stage.close();
    }

    @FXML
    public void onAcceptClick() {
        if (currentUser != null) {
            try {
                Datasource<UserList> datasourceUserlist = new UserListFileDatasource("data", currentRole + ".csv");
                currentUser.setFirstname(firstNameTextField.getText());
                currentUser.setLastname(lastNameTextField.getText());
                currentUser.setUsername(userNameTextField.getText());
                currentUser.setPassword(startPasswordTextField.getText());
                if (currentUser instanceof FacultyUser) {
                    if (facultyChoiceBox.getValue() == null) throw new UserException("กรุณาเลือกคณะที่ต้องการแก้ไข");
                    ((FacultyUser)currentUser).setFaculty(facultyChoiceBox.getValue());
                }
                if (currentUser instanceof DepartmentUser) {
                    if (departmentChoiceBox.getValue() == null) throw new UserException("กรุณาเลือกภาควิชาที่ต้องการแก้ไข");
                    ((DepartmentUser)currentUser).setDepartment(departmentChoiceBox.getValue());
                }
                if (currentRole.equals("advisor")) currentUser.setId(advisorIdTextField.getText());
                datasourceUserlist.writeData(userList);
                if (currentRole.equals("department-staff")) {
                    currentControllStaffPage.loadDepartmentStaff();
                } else if (currentRole.equals("faculty-staff")) {
                    currentControllStaffPage.loadFacultyStaff();
                } else if (currentRole.equals("advisor")) {
                    currentControllStaffPage.loadAdvisor();
                }
                currentControllStaffPage.resetSearch();
                stage.close();
            } catch (UserException e) {
                errorLabel.setVisible(true);
                errorLabel.setText(e.getMessage());
            }
        } else if (faculty != null) {
            try {
                Datasource<FacultyList> datasourceFacultylist = new FacultyListFileDatasource("data");
                Datasource<DepartmentList> departmentData = new DepartmentListFileDatasource("data");
                DepartmentList departmentUpdateList = departmentData.readData();
                String oldname = faculty.getName();
                faculty.setName(facultyNameTextField.getText());
                faculty.setId(facultyIdTextField.getText());
                departmentUpdateList.updateFaculty(faculty, oldname);
                departmentData.writeData(departmentUpdateList);
                datasourceFacultylist.writeData(facultyList);
                currentControllFacultyPage.loadFaculty();
                currentControllFacultyPage.resetSearch();
                stage.close();
            } catch (IllegalArgumentException | NoFacultyException e) {
                errorLabel.setVisible(true);
                errorLabel.setText(e.getMessage());
            }

        } else if (departmentList != null) {
            try {
                Datasource<DepartmentList> datasourceDepartmentlist = new DepartmentListFileDatasource("data");
                department.setName(departmentNameTextField.getText());
                if (facultyChoiceBox != null) department.setFaculty(facultyChoiceBox.getValue());
                else throw new IllegalArgumentException("กรุณาเลือกคณะที่ต้องการแก้ไข");
                department.setId(departmentIdTextField.getText());
                datasourceDepartmentlist.writeData(departmentList);
                currentControllFacultyPage.loadDepartment();
                currentControllFacultyPage.resetSearch();
                stage.close();
            } catch (NoFacultyException | IllegalArgumentException e) {
                errorLabel.setVisible(true);
                errorLabel.setText(e.getMessage());
            }
        }
    }
}
