package ku.cs.controllers.admin;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.faculty.Faculty;
import ku.cs.models.faculty.FacultyList;
import ku.cs.models.request.RequestList;
import ku.cs.models.user.Admin;
import ku.cs.models.user.FacultyUser;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.services.FacultyListFileDatasource;
import ku.cs.services.RequestListFileDatasource;
import ku.cs.services.UserListFileDatasource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DashBoardController {
    User loginUser;

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
    private TableView<Faculty> userInFacultyTableView;

    public void initializeDashBoard() {
        UserListFileDatasource userDatasource = new UserListFileDatasource("data", "admin.csv");
        UserList userList = userDatasource.readAllUser();
        userList.deleteUserByObject(userList.findUserByUUID(loginUser.getUUID()));
        allUsersLabel.setText(String.valueOf(userList.getUsers().size()));
        facultyStaffLabel.setText(String.valueOf(userList.getUsers("faculty-staff").size()));
        departmentStaffLabel.setText(String.valueOf(userList.getUsers("department-staff").size()));
        advisorLabel.setText(String.valueOf(userList.getUsers("advisor").size()));
        studentLabel.setText(String.valueOf(userList.getUsers("student").size()));

        RequestListFileDatasource requestDatasource = new RequestListFileDatasource("data");
        RequestList requestList = requestDatasource.readData();
        int amountSuccess = requestList.getRequests("คำร้องดำเนินการครบถ้วน").size();
        int amountReject = requestList.getRequests("คำร้องถูกปฏิเสธ").size();
        allRequestLabel.setText(String.valueOf(requestList.getRequests().size()));
        processRequestLabel.setText(String.valueOf(requestList.getRequests().size() - (amountReject + amountSuccess)));
        sucessRequestLabel.setText(String.valueOf(amountSuccess));
        rejectRequestLabel.setText(String.valueOf(amountReject));

        showTableAmountUserInFaculty();
        userTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue == facultyUserTab) {
                    showTableAmountUserInFaculty();
                } else if (newValue == departmentUserTab) {

                }
            }
        });
    }

    public void setLoginUser(Admin loginUser) {
        this.loginUser = loginUser;
    }

    private void showTableAmountUserInFaculty() {
        FacultyListFileDatasource facDatasource = new FacultyListFileDatasource("data");
        FacultyList facultyList = facDatasource.readData();

        TableColumn<Faculty, String> facultyNameColumn = new TableColumn<>("ชื่อคณะ");
        facultyNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Faculty, Integer> amountUserFacultyColumn = new TableColumn<>("จำนวนผู้ใช้");
        amountUserFacultyColumn.setCellValueFactory(new PropertyValueFactory<>("UsersCount"));

        userInFacultyTableView.getColumns().clear();
        userInFacultyTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        userInFacultyTableView.getColumns().addAll(facultyNameColumn, amountUserFacultyColumn);
        userInFacultyTableView.getItems().addAll(facultyList.getFacultyList());


    }
}
