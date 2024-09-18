package ku.cs.controllers.advisor;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.models.user.Student;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.services.Datasource;
import ku.cs.services.RequestListFileDatasource;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class AdvisorStudentRequestsController {
    @FXML
    TableView requestListTableView;
    @FXML
    BorderPane borderPane;
    @FXML
    Label displayName;

    private Datasource<RequestList> requestListDatasource;
    private Datasource<UserList> datasource;
    private RequestList requestList;
    private ArrayList<String> studentId;
    private UserList userlist;
    private String selectedStudentId;
    private String studentName;

    public void initialize() {
        studentId = new ArrayList<>();
        showTable();
    }

    public void setStudentName(String studentName){
        this.studentName = studentName;
        displayName.setText(this.studentName);
    }

    public void setSelectedStudentId(String selectedStudentId) {
        this.selectedStudentId = selectedStudentId;
        loadRequests();
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
        TableColumn<Request, String> statusNextColumn = new TableColumn<>("สถานะคำร้องต่อไป");
        statusNextColumn.setCellValueFactory(new PropertyValueFactory<>("statusNext"));

        nameColumn.setMinWidth(150);
        dateColumn.setMinWidth(200);
        typeColumn.setMinWidth(150);
        statusColumn.setMinWidth(190);
        statusNextColumn.setMinWidth(241);

        requestListTableView.getColumns().add(nameColumn);
        requestListTableView.getColumns().add(dateColumn);
        requestListTableView.getColumns().add(typeColumn);
        requestListTableView.getColumns().add(statusColumn);
        requestListTableView.getColumns().add(statusNextColumn);
    }

    private void loadRequests() {
        requestListDatasource = new RequestListFileDatasource("data");
        requestList = requestListDatasource.readData();
        requestListTableView.getItems().clear();

        if (requestList != null) {
            for (Request request : requestList.getRequests()) {
                if (selectedStudentId.equals(request.getNisitId())){
                    requestListTableView.getItems().add(request);
                }
            }
        }
    }


    public void onBackButtonClick(){
        try {
            String viewPath = "/ku/cs/views/advisor-students-pane.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            AdvisorStudentListController controller = fxmlLoader.getController();
            controller.initialize();
            borderPane.setCenter(pane);
            controller.setBorderPane(borderPane);
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

}
