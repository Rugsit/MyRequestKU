package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.models.faculty.Faculty;
import ku.cs.models.user.*;
import ku.cs.services.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AdminManageStaffController {
    // store data what object that currently login now
    private Admin loginUser;

    // dataList for write data to file
    private UserList userList;

    // FXML Component
    @FXML
    private Stage currentPopupStage;
    @FXML
    private TableView<User> userListTableview;
    @FXML
    private TabPane staffTabPane;
    @FXML
    private Tab advisorTab;
    @FXML
    private Tab departmentTab;
    @FXML
    private Tab facultyTab;
    @FXML
    private TextField searchTextField;
    @FXML
    private Button addButton;
    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private Label test;

    public void initialize() {
        System.out.println(test.getStyleClass());


        SetTransition transition = new SetTransition();
        transition.setButtonBounce(addButton);

        Label placeHolder = new Label("ไม่พบข้อมูล");
        userListTableview.setPlaceholder(placeHolder);
        userListTableview.setFocusTraversable(true);

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

        userListTableview.setRowFactory(tv -> {
            TableRow<User> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    User selectedUser = row.getItem();
                    showEditPopupWhenClickRow(selectedUser);
                }
            });
            return row;
        });

        userListTableview.setOnKeyPressed(e -> {
            User user = userListTableview.getSelectionModel().getSelectedItem();
            if (user != null && e.getCode() == KeyCode.ENTER) {
                showEditPopupWhenClickRow(user);
            }
        });

    }

    public void setLoginUser(Admin loginUser) {
        this.loginUser = loginUser;
    }

    private void showEditPopupWhenClickRow(User selectedUser) {
        if (selectedUser != null) {
            if (selectedUser.getRole().equals("faculty-staff")) {
                showEditPopup(selectedUser, "faculty-staff");
            } else if (selectedUser.getRole().equals("department-staff")) {
                showEditPopup(selectedUser, "department-staff");
            } else if (selectedUser.getRole().equals("advisor")) {
                showEditPopup(selectedUser, "advisor");
            }
        }
    }

    private void search(String newValue) {
        Set<User> filter = userList.getUsers()
                .stream()
                .filter(user -> user.getName().toLowerCase().contains(newValue.toLowerCase()) ||
                        user.getUsername().toLowerCase().contains(newValue.toLowerCase()))
                .collect(Collectors.toSet());

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
        Datasource<UserList> datasource = new UserListFileDatasource("data", role+".csv");
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
                currentPopupStage.setScene(scene);
                currentPopupStage.initModality(Modality.APPLICATION_MODAL);
                currentPopupStage.setTitle("Confirm");
                currentPopupStage.show();
            }
        } catch (IOException ee) {
        ee.printStackTrace();
        }
    }

    public void resetSearch() {
        searchTextField.setText("");
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
                controller.setListForWrite(userList);
                controller.setCurrentControllpage(this);
                controller.setRole(addStaffRole);
                controller.setChoiceBox();
                scene.getStylesheets().add(getClass().getResource("/ku/cs/styles/error-confirm-edit-page-style.css").toExternalForm());
                currentPopupStage.setScene(scene);
                currentPopupStage.initModality(Modality.APPLICATION_MODAL);
                currentPopupStage.setTitle("Confirm");
                currentPopupStage.show();
            }
        } catch (IOException ee) {
            System.err.println(Arrays.toString(ee.getStackTrace()));
        }
    }

    @FXML
    protected void goToAdminManageUsers() {
        try {
            FXRouter.goTo("admin-manage-users", loginUser);
        } catch (IOException e) {
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
    protected void goToUserProfile() {
        try {
            FXRouter.goTo("admin-user-profile", loginUser);
        } catch (IOException e) {
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
