package ku.cs.controllers.admin;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import ku.cs.models.department.Department;
import ku.cs.models.department.DepartmentList;
import ku.cs.models.faculty.Faculty;
import ku.cs.models.faculty.FacultyList;
import ku.cs.models.request.RequestList;
import ku.cs.models.user.Admin;
import ku.cs.models.user.FacultyUser;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.services.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class DashBoardController {
    private User loginUser;
    private long  lastModified = 0;

    @FXML
    private TabPane userTabPane;
    @FXML
    private Label advisorLabel;
    @FXML
    private Label allRequestLabel;
    @FXML
    private Label allUsersLabel;
    @FXML
    private Label departmentStaffLabel;
    @FXML
    private Label facultyStaffLabel;
    @FXML
    private Label processRequestLabel;
    @FXML
    private Label rejectRequestLabel;
    @FXML
    private Label studentLabel;
    @FXML
    private Label sucessRequestLabel;
    @FXML
    private Tab facultyUserTab;
    @FXML
    private Tab departmentUserTab;
    @FXML
    private Tab facultyRequestTab;
    @FXML
    private Tab departmentRequestTab;
    @FXML
    private TableView<Faculty> userInFacultyTableView, requestFacultyTableView;
    @FXML
    private TableView<Department> userInDepartmentTableView, requestDepartmentTableView;
    @FXML
    private TabPane requestTabPane;
    @FXML
    private AnchorPane mainAnchorPane;

    public void initializeDashBoard() throws IOException {

        TableColumn<Faculty, String> facultyNameColumn = new TableColumn<>("ชื่อคณะ");
        facultyNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Faculty, Integer> amountUserFacultyColumn = new TableColumn<>("จำนวนผู้ใช้");
        amountUserFacultyColumn.setCellValueFactory(new PropertyValueFactory<>("UsersCount"));
        userInFacultyTableView.getColumns().addAll(facultyNameColumn, amountUserFacultyColumn);

        TableColumn<Department, String> departmentNameColumn = new TableColumn<>("ชื่อภาควิชา");
        departmentNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Department, Integer> amountUserDepartmentColumn = new TableColumn<>("จำนวนผู้ใช้");
        amountUserDepartmentColumn.setCellValueFactory(new PropertyValueFactory<>("UsersCount"));
        userInDepartmentTableView.getColumns().addAll(departmentNameColumn, amountUserDepartmentColumn);

        TableColumn<Department, String> departmentRequestNameColumn = new TableColumn<>("ชื่อภาควิชา");
        departmentRequestNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Faculty, Integer> amountRequestFacultyColumn = new TableColumn<>("จำนวนคำร้อง");
        amountRequestFacultyColumn.setCellValueFactory(new PropertyValueFactory<>("RequestsCount"));
        TableColumn<Department, Integer> amountRequestDepartmentColumn = new TableColumn<>("จำนวนคำร้อง");
        amountRequestDepartmentColumn.setCellValueFactory(new PropertyValueFactory<>("RequestsCount"));
        requestFacultyTableView.getColumns().addAll(facultyNameColumn, amountRequestFacultyColumn);
        requestDepartmentTableView.getColumns().addAll(departmentRequestNameColumn, amountRequestDepartmentColumn);


        userInFacultyTableView.setPlaceholder(new Label("กำลังโหลดข้อมูล..."));
        userInDepartmentTableView.setPlaceholder(new Label("กำลังโหลดข้อมูล..."));
        requestFacultyTableView.setPlaceholder(new Label("กำลังโหลดข้อมูล..."));
        requestDepartmentTableView.setPlaceholder(new Label("กำลังโหลดข้อมูล..."));
        updateEmptyUI(); // อัปเดต UI เปล่า เช่นตัวเลขใน label อื่น ๆ

        Task<Void> loadDataTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                // โหลดข้อมูลหนัก ๆ ใน background
                showTableAmountUser(facultyUserTab);
                showTableAmountUser(departmentUserTab);
                showTableAmountUser(facultyRequestTab);
                showTableAmountUser(departmentRequestTab);
                return null;
            }

            @Override
            protected void succeeded() {
                Platform.runLater(() -> {
                    // อัปเดต TableView หลังจากโหลดข้อมูลเสร็จ
                    updatePage();

                    // Refresh TableView ให้แสดงข้อมูล
                    userInFacultyTableView.refresh();
                    userInDepartmentTableView.refresh();
                    requestFacultyTableView.refresh();
                    requestDepartmentTableView.refresh();
                });
            }
        };

        // รัน Task ใน background thread
        Thread thread = new Thread(loadDataTask);
        thread.setDaemon(true);
        thread.start();


        userTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue == facultyUserTab) {
                    userInDepartmentTableView.setVisible(false);
                    userInFacultyTableView.setVisible(true);
                } else if (newValue == departmentUserTab) {
                    userInDepartmentTableView.setVisible(true);
                    userInFacultyTableView.setVisible(false);
                }
                showTableAmountUser(newValue);
            }
        });


        requestTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue == facultyRequestTab) {
                    requestFacultyTableView.setVisible(true);
                    requestDepartmentTableView.setVisible(false);
                } else if (newValue == departmentRequestTab) {
                    requestFacultyTableView.setVisible(false);
                    requestDepartmentTableView.setVisible(true);
                }
                showTableAmountUser(newValue);
            }
        });

        File requestFile = new File("data/requests/requests.csv");
        File advisorFile = new File("data/users/advisor.csv");
        File departmentStaffFile = new File("data/users/department-staff.csv");
        File facultyStaffFile = new File("data/users/faculty-staff.csv");
        File studentFile = new File("data/users/student.csv");

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                long modified = requestFile.lastModified() + advisorFile.lastModified() + departmentStaffFile.lastModified() + facultyStaffFile.lastModified() + studentFile.lastModified();
                if (modified != lastModified) {
                    lastModified = modified;
                    Platform.runLater(() -> {
                        updatePage();
                        System.out.println("CSV file has been modified!");
                        // สามารถโหลดข้อมูลใหม่หรืออัปเดต UI ได้ที่นี่
                    });
                }
            }
        };

        Timer timer = new Timer(true);
        timer.schedule(task, 0, 500);
    }

    public void setLoginUser(Admin loginUser) {
        this.loginUser = loginUser;
    }

    private void showTableAmountUser(Tab currentTab) {
        Datasource<FacultyList> facDatasource = new FacultyListFileDatasource("data");
        FacultyList facultyList = facDatasource.readData();
        Datasource<DepartmentList> departmentListFileDatasource = new DepartmentListFileDatasource("data");
        DepartmentList departmentList = departmentListFileDatasource.readData();

        Platform.runLater(() -> {
            if (facultyUserTab == currentTab) {
                userInFacultyTableView.getItems().clear();
                userInFacultyTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                userInFacultyTableView.getItems().addAll(facultyList.getFacultyList());
            } else if (departmentUserTab == currentTab) {
                userInDepartmentTableView.getItems().clear();
                userInDepartmentTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                userInDepartmentTableView.getItems().addAll(departmentList.getDepartments());
            } else if (facultyRequestTab == currentTab) {
                requestFacultyTableView.getItems().clear();
                requestFacultyTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                requestFacultyTableView.getItems().addAll(facultyList.getFacultyList());
            } else if (departmentRequestTab == currentTab) {
                requestDepartmentTableView.getItems().clear();
                requestDepartmentTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                requestDepartmentTableView.getItems().addAll(departmentList.getDepartments());
            }
        });
    }

    private void updatePage() {
        if (facultyUserTab.isSelected()) {
            showTableAmountUser(facultyUserTab);
        }if (departmentUserTab.isSelected()) {
            showTableAmountUser(departmentUserTab);
        }if (facultyRequestTab.isSelected()) {
            showTableAmountUser(facultyRequestTab);
        }if (departmentRequestTab.isSelected()) {
            showTableAmountUser(facultyRequestTab);
        }
        UserListFileDatasource userDatasource = new UserListFileDatasource("data", "admin.csv");
        UserList userList = userDatasource.readAllUser();
        userList.deleteUserByObject(userList.findUserByUUID(loginUser.getUUID()));


        allUsersLabel.setText(String.valueOf(userList.getUsers().stream().filter(user -> !user.getUsername().equals("no-username")).toList().size()));
        facultyStaffLabel.setText(String.valueOf(userList.getUsers("faculty-staff").size()));
        departmentStaffLabel.setText(String.valueOf(userList.getUsers("department-staff").size()));
        advisorLabel.setText(String.valueOf(userList.getUsers("advisor").size()));
        studentLabel.setText(String.valueOf(userList.getUsers("student").stream().filter(user -> !user.getUsername().equals("no-username")).toList().size()));

        Datasource<RequestList> requestDatasource = new RequestListFileDatasource("data");
        RequestList requestList = requestDatasource.readData();
        int amountSuccess = requestList.getRequests("คำร้องดำเนินการครบถ้วน").size();
        int amountReject = requestList.getRequests("คำร้องถูกปฏิเสธ").size();
        allRequestLabel.setText(String.valueOf(requestList.getRequests().size()));
        processRequestLabel.setText(String.valueOf(requestList.getRequests().size() - (amountReject + amountSuccess)));
        sucessRequestLabel.setText(String.valueOf(amountSuccess));
        rejectRequestLabel.setText(String.valueOf(amountReject));
    }

    private void updateEmptyUI() {
        // อัปเดต UI เปล่า เช่นตัวเลขใน label ต่าง ๆ โดยไม่โหลดข้อมูลจริง ๆ ก่อน
        allUsersLabel.setText("กำลังโหลด...");
        facultyStaffLabel.setText("กำลังโหลด...");
        departmentStaffLabel.setText("กำลังโหลด...");
        advisorLabel.setText("กำลังโหลด...");
        studentLabel.setText("กำลังโหลด...");
        allRequestLabel.setText("กำลังโหลด...");
        processRequestLabel.setText("กำลังโหลด...");
        sucessRequestLabel.setText("กำลังโหลด...");
        rejectRequestLabel.setText("กำลังโหลด...");
    }
}
