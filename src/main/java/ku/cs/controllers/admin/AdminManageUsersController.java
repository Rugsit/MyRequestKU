package ku.cs.controllers.admin;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import ku.cs.models.user.Admin;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.services.FXRouter;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

public class AdminManageUsersController {
    Admin loginUser;
    private UserListFileDatasource datasource;
    private UserList userlist;

    @FXML
    private TableView<User> userListTableView;
    @FXML
    private TextField searchTextField;
    @FXML
    private Tab facultyStaffTab;
    @FXML
    private Tab departmentStaffTab;
    @FXML
    private Tab adviserTab;
    @FXML
    private Tab studentTab;
    @FXML
    private Tab allTab;
    @FXML
    private TabPane userListTabPane;

    @FXML
    public void initialize() {
        if (FXRouter.getData() instanceof Admin) loginUser = (Admin) FXRouter.getData();

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
        TableColumn<User, LocalDateTime> lastTime = new TableColumn<>("เวลาที่เข้าใช้ล่าสุด");
        lastTime.setCellValueFactory(new PropertyValueFactory<>("lastLogin"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH:mm:ss");

        // Set the cellFactory to format the LocalDateTime
        lastTime.setCellFactory(column -> new TextFieldTableCell<>(new StringConverter<LocalDateTime>() {
            @Override
            public String toString(LocalDateTime lastLogin) {
                return lastLogin != null ? lastLogin.format(formatter) : "";
            }

            @Override
            public LocalDateTime fromString(String string) {
                return LocalDateTime.parse(string, formatter);
            }
        }));

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
                search();
            } else {
                userListTableView.getItems().clear();
                userListTableView.getItems().addAll(userlist.getUsers());
                TableColumn<User, LocalDateTime> lastTimeInSearch = (TableColumn<User, LocalDateTime>) userListTableView.getColumns().get(4);
                lastTimeInSearch.setSortType(TableColumn.SortType.DESCENDING);
                userListTableView.getSortOrder().add(lastTime);
            }
        });
    }

    private void loadAllUsers() {
        datasource = new UserListFileDatasource("data", "admin.csv");
        userlist = datasource.readAllUser();
        //MARK !!!!
        Collection<User> HashUser = userlist.getUsers();
        //MARK !!!!
        HashUser.removeIf(user -> user.getRole().equals("admin"));
        updateTableView();
    }

    private void loadSpecificUsers(String fileName) {
        datasource = new UserListFileDatasource("data", fileName);
        userlist = datasource.readData();
        updateTableView();
    }

    private void search() {
        HashSet<User> filter = new HashSet<>();
        for (User user : userlist.getUsers()) {
            if (user.getName().toLowerCase().contains(searchTextField.getText().toLowerCase())) {
                filter.add(user);
            }
        }
        userListTableView.getItems().clear();
        userListTableView.getItems().addAll(filter);
        TableColumn<User, LocalDateTime> lastTime = (TableColumn<User, LocalDateTime>) userListTableView.getColumns().get(4);
        lastTime.setSortType(TableColumn.SortType.DESCENDING);
        userListTableView.getSortOrder().add(lastTime);
    }

    private void updateTableView() {

        if (searchTextField.getText().trim().isEmpty()) {
        userListTableView.getItems().clear();
        userListTableView.getItems().addAll(userlist.getUsers());

        TableColumn<User, LocalDateTime> lastTime = (TableColumn<User, LocalDateTime>) userListTableView.getColumns().get(4);
        lastTime.setSortType(TableColumn.SortType.DESCENDING);
        userListTableView.getSortOrder().add(lastTime);
        } else{
            search();
        }
    }
//CONFLICTS FOR NEST !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private void writeSpecificUsers(String fileName) {
        datasource = new UserListFileDatasource("data", fileName);
        if (fileName.equals("faculty-staff.csv")) {
            datasource.writeData(userlist.getUserList("faculty-staff"));
        } else if (fileName.equals("department-staff.csv")) {
            datasource.writeData(userlist.getUserList("department-staff"));
        } else if (fileName.equals("student.csv")) {
            datasource.writeData(userlist.getUserList("student"));
        } else if (fileName.equals("advisor.csv")) {
            datasource.writeData(userlist.getUserList("advisor"));
        }
    }

    private void writeAllUsers(User user) {
        String role = user.getRole();

//        UserList tmp;
//        tmp = new UserList();
//        tmp.getUsers(role).addAll(userlist.getUsers(role));
//        datasource.writeData(tmp);
//        สามารถเรียก ref ของ collection ภายในมาใช้ได้แต่รกนิสนึง

        if (role.equals("faculty-staff")) {
            datasource = new UserListFileDatasource("data", "faculty-staff.csv");
//            datasource.writeData(userlist.getFacultyList());
            datasource.writeData(userlist.getUserList(role));
        } else if (role.equals("department-staff")) {
            datasource = new UserListFileDatasource("data", "department-staff.csv");
//            datasource.writeData(userlist.getDepartmentList());
            datasource.writeData(userlist.getUserList(role));
        } else if (role.equals("student")) {
            datasource = new UserListFileDatasource("data", "student.csv");
//            datasource.writeData(userlist.getStudentList());
            datasource.writeData(userlist.getUserList(role));
        } else if (role.equals("advisor")) {
            datasource = new UserListFileDatasource("data", "advisor.csv");
//            datasource.writeData(userlist.getAdvisorList());
            datasource.writeData(userlist.getUserList(role));

        }
    }
//CONFLICTS FOR NEST !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    @FXML
    public void banButton() {
        User currentUser = userListTableView.getSelectionModel().getSelectedItem();
        if (currentUser == null) return;
        User targetUser = userlist.findUserByObject(currentUser);
        targetUser.setActive(false);

        Tab currentTab = userListTabPane.getSelectionModel().getSelectedItem();
        if (currentTab == allTab) {
            writeAllUsers(targetUser);
        } else if (currentTab == facultyStaffTab) {
            writeSpecificUsers("faculty-staff.csv");
        } else if (currentTab == departmentStaffTab) {
            writeSpecificUsers("department-staff.csv");
        } else if (currentTab == studentTab) {
            writeSpecificUsers("student.csv");
        } else if (currentTab == adviserTab) {
            writeSpecificUsers("advisor.csv");
        }
        updateTableView();
    }

    @FXML
    public void unbanButton() {
        User currentUser = userListTableView.getSelectionModel().getSelectedItem();
        if (currentUser == null) return;
        User targetUser = userlist.findUserByObject(currentUser);
        targetUser.setActive(true);

        Tab currentTab = userListTabPane.getSelectionModel().getSelectedItem();
        if (currentTab == allTab) {
            writeAllUsers(targetUser);
        } else if (currentTab == facultyStaffTab) {
            writeSpecificUsers("faculty-staff.csv");
        } else if (currentTab == departmentStaffTab) {
            writeSpecificUsers("department-staff.csv");
        } else if (currentTab == studentTab) {
            writeSpecificUsers("student.csv");
        } else if (currentTab == adviserTab) {
            writeSpecificUsers("advisor.csv");
        }
        updateTableView();
    }


    @FXML
    protected void goToAdminManageStaff() {
        try {
            FXRouter.goTo("admin-manage-staff", loginUser);
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void goToAdminManageFaculty() {
        try {
            FXRouter.goTo("admin-manage-faculty-department", loginUser);
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void goToUserProfile() {
        try {
            FXRouter.goTo("admin-user-profile", loginUser);
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
