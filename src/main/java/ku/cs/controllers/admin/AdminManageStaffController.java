package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.models.user.Advisor;
import ku.cs.models.user.FacultyUser;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.services.FXRouter;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;
import java.util.HashSet;

public class AdminManageStaffController {
    @FXML
    Stage currentPopupStage;
    UserListFileDatasource datasource;
    UserList userList;

    @FXML
    TableView<User> userListTableview;
    @FXML
    TabPane staffTabPane;
    @FXML
    private Tab advisorTab;

    @FXML
    private Tab departmentTab;

    @FXML
    private Tab facultyTab;

    @FXML
    TextField searchTextField;

    @FXML
    public void initialize() {
        Label placeHolder = new Label("ไม่พบข้อมูล");
        userListTableview.setPlaceholder(placeHolder);

        loadFacultyStaff();
        staffTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == facultyTab) {
                loadFacultyStaff();
            } else if (newValue == departmentTab) {
                loadDepartmentStaff();
            } else if (newValue == advisorTab) {
                loadAdvisor();
            }
        });
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.trim().isEmpty()) {
                search(newValue);
            } else {
                userListTableview.getItems().clear();
                userListTableview.getItems().addAll(userList.getUsers());
            }
        });

        userListTableview.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.getRole().equals("faculty-staff")) {
                    showEditPopup(newValue, "faculty-staff");
                } else if (newValue.getRole().equals("department-staff")) {
                    showEditPopup(newValue, "department-staff");
                } else if (newValue.getRole().equals("advisor")) {
                    showEditPopup(newValue, "advisor");
                }
            }
        });
    }

    private void search(String newValue) {
        HashSet<User> filter = new HashSet<>();
        for (User user : userList.getUsers()) {
            if (user.getName().toLowerCase().contains(newValue.toLowerCase())) {
                filter.add(user);
            }
        }
        userListTableview.getItems().clear();
        userListTableview.getItems().addAll(filter);
    }

    public void loadFacultyStaff() {
        userListTableview.getItems().clear();
        userListTableview.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<User, String> name = new TableColumn<>("ชื่อ");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<User, String> username = new TableColumn<>("ชื่อผู้ใช้");
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableColumn<User, String> startPassword = new TableColumn<>("รหัสผ่านเริ่มต้น");
        startPassword.setCellValueFactory(new PropertyValueFactory<>("DefaultPassword"));
        TableColumn<User, String> faculty = new TableColumn<>("คณะ");
        faculty.setCellValueFactory(new PropertyValueFactory<>("Faculty"));

        userListTableview.getColumns().setAll(name, username, startPassword, faculty);
        readSpecificRole("faculty-staff");
        userListTableview.getItems().addAll(userList.getUsers());
        search(searchTextField.getText());
    }

    public void loadDepartmentStaff() {
        userListTableview.getItems().clear();
        userListTableview.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<User, String> name = new TableColumn<>("ชื่อ");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<User, String> username = new TableColumn<>("ชื่อผู้ใช้");
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableColumn<User, String> startPassword = new TableColumn<>("รหัสผ่านเริ่มต้น");
        startPassword.setCellValueFactory(new PropertyValueFactory<>("DefaultPassword"));
        TableColumn<User, String> faculty = new TableColumn<>("คณะ");
        faculty.setCellValueFactory(new PropertyValueFactory<>("faculty"));
        TableColumn<User, String> department = new TableColumn<>("ภาควิชา");
        department.setCellValueFactory(new PropertyValueFactory<>("department"));

        userListTableview.getColumns().setAll(name, username, startPassword ,faculty, department);
        readSpecificRole("department-staff");
        userListTableview.getItems().addAll(userList.getUsers());
        search(searchTextField.getText());
    }


    public void loadAdvisor() {
        userListTableview.getItems().clear();
        userListTableview.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<User, String> name = new TableColumn<>("ชื่อ");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<User, String> username = new TableColumn<>("ชื่อผู้ใช้");
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableColumn<User, String> startPassword = new TableColumn<>("รหัสผ่านเริ่มต้น");
        startPassword.setCellValueFactory(new PropertyValueFactory<>("DefaultPassword"));
        TableColumn<User, String> faculty = new TableColumn<>("คณะ");
        faculty.setCellValueFactory(new PropertyValueFactory<>("faculty"));
        TableColumn<User, String> department = new TableColumn<>("ภาควิชา");
        department.setCellValueFactory(new PropertyValueFactory<>("department"));
        TableColumn<User, String> advisorId = new TableColumn<>("รหัสประจำตัว");
        advisorId.setCellValueFactory(new PropertyValueFactory<>("id"));

        userListTableview.getColumns().setAll(name, username,startPassword, faculty, department, advisorId);
        readSpecificRole("advisor");
        userListTableview.getItems().addAll(userList.getUsers());
        search(searchTextField.getText());
    }

    private void readSpecificRole(String role) {
        datasource = new UserListFileDatasource("data", role+".csv");
        userList = datasource.readData();
    }

    private void showEditPopup(User currentUser, String role) {
        try {
            if (currentPopupStage == null || !currentPopupStage.isShowing()) {
                currentPopupStage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/edit-" + role + ".fxml"));
                Scene scene = new Scene(fxmlLoader.load());

                EditFormController controller = fxmlLoader.getController();
                controller.setCurrentObject(currentUser);
                controller.setStage(currentPopupStage);
                controller.setListForWrite(userList);
                controller.showOldUserData(role);
                controller.setChoiceBox();
                controller.setCurrentControllPage(this);
                scene.getStylesheets().add(getClass().getResource("/ku/cs/styles/error-confirm-edit-page-style.css").toExternalForm());
                currentPopupStage.setScene(scene);
                currentPopupStage.initModality(Modality.APPLICATION_MODAL);
                currentPopupStage.setTitle("Confirm");
                currentPopupStage.show();
            }
        } catch (IOException ee) {
            System.err.println("Error: " + ee.getMessage());
        }
    }

    @FXML
    public void addStaff() {
        Tab tab = staffTabPane.getSelectionModel().getSelectedItem();
        String addStaffRole = tab == facultyTab ? "faculty-staff" : tab == departmentTab ? "department-staff" : "advisor";
        try {
            if (currentPopupStage == null || !currentPopupStage.isShowing()) {
                currentPopupStage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/add-" + addStaffRole + ".fxml"));
                Scene scene = new Scene(fxmlLoader.load());

                AddFormController controller = fxmlLoader.getController();
                controller.setStage(currentPopupStage);
                controller.setUserListForWrite(userList);
                controller.setCurrentControllAdminpage(this);
                controller.setRole(addStaffRole);
                controller.setChoiceBox();
                scene.getStylesheets().add(getClass().getResource("/ku/cs/styles/error-confirm-edit-page-style.css").toExternalForm());
                currentPopupStage.setScene(scene);
                currentPopupStage.initModality(Modality.APPLICATION_MODAL);
                currentPopupStage.setTitle("Confirm");
                currentPopupStage.show();
            }
        } catch (IOException ee) {
            System.err.println("Error: " + ee.getMessage());
        }
    }

    @FXML
    protected void goToAdminManageUsers() {
        try {
            FXRouter.goTo("admin-manage-users");
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void goToAdminManageFaculty() {
        try {
            FXRouter.goTo("admin-manage-faculty-department");
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void goToUserProfile() {
        try {
            FXRouter.goTo("user-profile");
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
