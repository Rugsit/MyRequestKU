package ku.cs.controllers.admin;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.services.FXRouter;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class AdminManageUsersController {
    @FXML
    HashSet<User> storeCurrentUserList;
    @FXML
    TableView<User> userListTableView;

    @FXML
    TextField searchTextField;

    UserListFileDatasource datasource;

    @FXML
    Tab facultyStaffTab;
    @FXML
    Tab departmentStaffTab;
    @FXML
    Tab adviserTab;
    @FXML
    Tab studentTab;
    @FXML
    Tab allTab;

    @FXML
    TabPane userListTabPane;

    UserList userlist;

    @FXML
    public void initialize() {
        storeCurrentUserList = new HashSet<>();

        Label placeHolder = new Label("ไม่พบข้อมูล");
        userListTableView.setPlaceholder(placeHolder);
        userListTableView.getColumns().clear();
        userListTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<User, String> avatar = new TableColumn<>("รูปภาพผู้ใช้");
        avatar.setCellValueFactory(new PropertyValueFactory<>("avatar"));
        TableColumn<User, String> userName = new TableColumn<>("ชื่อผู้ใช้");
        userName.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableColumn<User, String> name = new TableColumn<>("ชื่อ-นามสกุล");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<User, Boolean> status = new TableColumn<>("สถานะการใช้งาน");
        status.setCellValueFactory(new PropertyValueFactory<>("ActiveStatus"));
        TableColumn<User, Date> lastTime = new TableColumn<>("เวลาที่เข้าใช้ล่าสุด");

        lastTime.setCellValueFactory(new PropertyValueFactory<>("LastLogin"));
        lastTime.setSortType(TableColumn.SortType.ASCENDING);

        userListTableView.setRowFactory(tv -> new TableRow<User>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                    getStyleClass().removeAll("row-active", "row-inactive");
                } else {
                    getStyleClass().removeAll("row-active", "row-inactive");
                    if (item.isActive()) {
                        getStyleClass().add("row-active");
                    } else {
                        getStyleClass().add("row-inactive");
                    }
                }
            }
        });

        userListTableView.getColumns().addAll(avatar, userName, name, status, lastTime);
        userListTableView.getSortOrder().add(lastTime);


        loadAllUsers();
        userListTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == allTab) {
                userListTableView.getItems().clear();
                loadAllUsers();
            } else if (newValue == facultyStaffTab) {
                loadSpecificUsers("faculty-staff.csv");
            } else if (newValue == departmentStaffTab) {
                loadSpecificUsers("department-staff.csv");
            } else if (newValue == studentTab) {
                loadSpecificUsers("student.csv");
            } else if (newValue == adviserTab) {
                loadSpecificUsers("advisor.csv");
            }
        });

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.trim().isEmpty()) {
                HashSet<User> filter = new HashSet<>();
                for (User user : userListTableView.getItems()) {
                    if (user.getName().toLowerCase().contains(newValue.toLowerCase())) {
                        filter.add(user);
                    }
                }
                userListTableView.getItems().clear();
                userListTableView.getItems().addAll(filter);
            } else {
                userListTableView.getItems().clear();
                userListTableView.getItems().addAll(storeCurrentUserList);
            }
        });
    }

    private void loadAllUsers() {
        userListTableView.getItems().clear();
        datasource = new UserListFileDatasource("data", "admin.csv");
        userlist = datasource.readAllUser();
        HashSet<User> HashUser = userlist.getUsers();
        HashUser.removeIf(user -> user.getRole().equals("admin"));
        userListTableView.getItems().addAll(userlist.getUsers());
        storeCurrentUserList.clear();
        storeCurrentUserList.addAll(userlist.getUsers());
    }

    private void loadSpecificUsers(String fileName) {
        userListTableView.getItems().clear();
        datasource = new UserListFileDatasource("data", fileName);
        userlist = datasource.readData();
        userListTableView.getItems().addAll(userlist.getUsers());
        storeCurrentUserList.clear();
        storeCurrentUserList.addAll(userlist.getUsers());
    }

    private void writeSpecificUsers(String fileName) {
        datasource = new UserListFileDatasource("data", fileName);
        datasource.writeData(userlist);
    }

    private void writeAllUsers(User user) {
        String role = user.getRole();
        HashSet<User> hashUser = userlist.getUsers();
        hashUser.removeIf(user1 -> !user1.getRole().equals(role));
        if (role.equals("faculty-staff")) {
            datasource = new UserListFileDatasource("data", "faculty-staff.csv");
            datasource.writeData(userlist);
        } else if (role.equals("department-staff")) {
            datasource = new UserListFileDatasource("data", "department-staff.csv");
            datasource.writeData(userlist);
        } else if (role.equals("student")) {
            datasource = new UserListFileDatasource("data", "student.csv");
            datasource.writeData(userlist);
        } else if (role.equals("advisor")) {
            datasource = new UserListFileDatasource("data", "advisor.csv");
            datasource.writeData(userlist);
        }
    }

    @FXML
    public void banButton() {
        User currentUser = userListTableView.getSelectionModel().getSelectedItem();
        if (currentUser == null) return;
        User targetUser = userlist.findUserById(currentUser.getId());
        targetUser.setActive(false);

        Tab currentTab = userListTabPane.getSelectionModel().getSelectedItem();
        if (currentTab == allTab) {
            writeAllUsers(targetUser);
            loadAllUsers();
        } else if (currentTab == facultyStaffTab) {
            writeSpecificUsers("faculty-staff.csv");
            loadSpecificUsers("faculty-staff.csv");
        } else if (currentTab == departmentStaffTab) {
            writeSpecificUsers("department-staff.csv");
            loadSpecificUsers("department-staff.csv");
        } else if (currentTab == studentTab) {
            writeSpecificUsers("student.csv");
            loadSpecificUsers("student.csv");
        } else if (currentTab == adviserTab) {
            writeSpecificUsers("advisor.csv");
            loadSpecificUsers("advisor.csv");
        }
    }

    @FXML
    public void unbanButton() {
        User currentUser = userListTableView.getSelectionModel().getSelectedItem();
        if (currentUser == null) return;
        User targetUser = userlist.findUserById(currentUser.getId());
        targetUser.setActive(true);

        Tab currentTab = userListTabPane.getSelectionModel().getSelectedItem();
        if (currentTab == allTab) {
            writeAllUsers(targetUser);
            loadAllUsers();
        } else if (currentTab == facultyStaffTab) {
            writeSpecificUsers("faculty-staff.csv");
            loadSpecificUsers("faculty-staff.csv");
        } else if (currentTab == departmentStaffTab) {
            writeSpecificUsers("department-staff.csv");
            loadSpecificUsers("department-staff.csv");
        } else if (currentTab == studentTab) {
            writeSpecificUsers("student.csv");
            loadSpecificUsers("student.csv");
        } else if (currentTab == adviserTab) {
            writeSpecificUsers("advisor.csv");
            loadSpecificUsers("advisor.csv");
        }
    }


    @FXML
    protected void goToAdminManageStaff() {
        try {
            FXRouter.goTo("admin-manage-staff");
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
