package ku.cs.controllers.advisor;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.models.user.Student;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.services.Datasource;
import ku.cs.services.RequestListFileDatasource;
import ku.cs.services.UserListFileDatasource;

import java.util.ArrayList;

public class AdvisorRequestsController {
    @FXML TableView requestListTableView;
    @FXML
    BorderPane borderPane;
    private Datasource<RequestList> requestListDatasource;
    private Datasource<UserList> datasource;
    private RequestList requestList;
    private ArrayList<String> studentId;
    private UserList userlist;

    public void initialize() {
        studentId = new ArrayList<>();
        getStudentID();
        loadRequests();
        showTable();
    }

    private void showTable() {
        requestListTableView.getColumns().clear();

        // Create and configure columns with correct type
        TableColumn<Request, String> nameColumn = new TableColumn<>("ชื่อ-นามสกุล");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Request, String> dateColumn = new TableColumn<>("วันที่ยื่นคำร้อง");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));
        TableColumn<Request, String> typeColumn = new TableColumn<>("ประเภทคำร้อง");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("requestType"));
        TableColumn<Request, String> statusColumn = new TableColumn<>("สถานะคำร้อง");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("statusNow"));

        nameColumn.setMinWidth(150);
        dateColumn.setMinWidth(200);
        typeColumn.setMinWidth(200);
        statusColumn.setMinWidth(381);

        requestListTableView.getColumns().add(nameColumn);
        requestListTableView.getColumns().add(dateColumn);
        requestListTableView.getColumns().add(typeColumn);
        requestListTableView.getColumns().add(statusColumn);
    }

    private void getStudentID(){
        datasource = new UserListFileDatasource("data", "student.csv");
        userlist = datasource.readData();
        for (User user : userlist.getUsers()) {
            if (user instanceof Student){
                Student student = (Student) user;
                if (AdvisorPageController.getAdvisorUUID().equals(student.getAdvisor())){
                    studentId.add(student.getId());
                }
            }
        }
    }

    private void loadRequests() {
        requestListDatasource = new RequestListFileDatasource("data");
        requestList = requestListDatasource.readData();
        requestListTableView.getItems().clear();

        if (requestList != null) {
            for (Request request : requestList.getRequests()) {
                if (studentId.contains(request.getNisitId())){
                    requestListTableView.getItems().add(request);
                }
            }
        }
    }


    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

}
