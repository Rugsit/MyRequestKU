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
import ku.cs.services.Datasource;
import ku.cs.services.DepartmentListFileDatasource;
import ku.cs.services.FacultyListFileDatasource;
import ku.cs.services.UserListFileDatasource;
import org.w3c.dom.Text;

public class EditFormController {
    AdminManageStaffController currentControllStaffPage;
    AdminManageFacultyController currentControllFacultyPage;
    UserListFileDatasource datasourceUserlist;
    FacultyListFileDatasource datasourceFacultylist;
    DepartmentListFileDatasource datasourceDepartmentlist;

    String currentRole;

    Faculty faculty;
    FacultyList facultyList;
    Department department;
    DepartmentList departmentList;
    User currentUser;
    UserList userList;

    Stage stage;

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

    public void showOldFacultyDepartmentData() {
        if (faculty == null) {
            facultyNameLabel.setText(department.getFaculty());
            departmentNameLabel.setText(department.getName());
            departmentIdLabel.setText(department.getId());
        } else if (department == null) {
            facultyNameLabel.setText(faculty.getName());
            facultyIdLabel.setText(faculty.getId());
        }
    }

    public void showOldUserData(String role) {
        currentRole = role;
        if (currentUser instanceof FacultyUser) {
            facultyLabel.setText(((FacultyUser)currentUser).getFaculty());
        }
        nameLabel.setText(currentUser.getName());
        userNameLabel.setText(currentUser.getUsername());
        startPasswordLabel.setText(currentUser.getDefaultPassword());
        if (currentUser instanceof DepartmentUser) {
            departmentLabel.setText(((DepartmentUser)currentUser).getDepartment());
        }
        if (currentUser instanceof Advisor) {
            advisorIdLabel.setText(currentUser.getId());
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
                datasourceUserlist = new UserListFileDatasource("data", currentRole + ".csv");
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
                stage.close();
            } catch (UserException e) {
                errorLabel.setVisible(true);
                errorLabel.setText(e.getMessage());
            }
        } else if (faculty != null) {
            datasourceFacultylist = new FacultyListFileDatasource("data");
            faculty.setName(facultyNameTextField.getText());
            faculty.setId(facultyIdTextField.getText());
            datasourceFacultylist.writeData(facultyList);
            currentControllFacultyPage.loadFaculty();
            stage.close();

        } else if (departmentList != null) {
            datasourceDepartmentlist = new DepartmentListFileDatasource("data");
            department.setName(departmentNameTextField.getText());
            department.setFaculty(facultyTextField.getText());
            department.setId(departmentIdTextField.getText());
            datasourceDepartmentlist.writeData(departmentList);
            currentControllFacultyPage.loadDepartment();
            stage.close();
        }
    }
}
