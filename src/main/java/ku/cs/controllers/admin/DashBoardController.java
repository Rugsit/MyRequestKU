package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import ku.cs.models.request.RequestList;
import ku.cs.models.user.Admin;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.services.RequestListFileDatasource;
import ku.cs.services.UserListFileDatasource;

public class DashBoardController {
    User loginUser;

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
    }

    public void setLoginUser(Admin loginUser) {
        this.loginUser = loginUser;
    }

}
