package ku.cs.controllers.advisor;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.StringConverter;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.models.user.Student;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.services.Datasource;
import ku.cs.services.RequestListFileDatasource;
import ku.cs.services.UserListFileDatasource;
import org.w3c.dom.Text;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class AdvisorStudentRequestsController {
    @FXML
    TableView requestListTableView;
    @FXML
    BorderPane borderPane;
    @FXML
    Label displayName;
    @FXML
    private TextField searchTextField;

    private Datasource<RequestList> requestListDatasource;
    private Datasource<UserList> datasource;
    private RequestList requestList;
    private ArrayList<String> studentId;
    private UserList userlist;
    private String selectedStudentId;
    private String studentName;

    public void initializeStudentRequests() {
        showTable();
    }

    public void setStudentName(String studentName){
        this.studentName = studentName;
        displayName.setText(this.studentName);
    }

    public void setSelectedStudentId(String selectedStudentId) {
        this.selectedStudentId = selectedStudentId;
        studentId = new ArrayList<>();
        loadRequests();
    }

    private void showTable() {
        requestListTableView.getColumns().clear();
        Label placeHolder = new Label("ไม่พบข้อมูล");
        requestListTableView.setPlaceholder(placeHolder);
        requestListTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Create and configure columns with correct type
        TableColumn<Request, String> nameColumn = new TableColumn<>("ชื่อ-นามสกุล");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Request, LocalDateTime> dateColumn = new TableColumn<>("วันที่ยื่นคำร้อง");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));
        TableColumn<Request, String> typeColumn = new TableColumn<>("ประเภทคำร้อง");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("requestType"));
        TableColumn<Request, String> statusColumn = new TableColumn<>("สถานะคำร้อง");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("statusNow"));
        TableColumn<Request, String> statusNextColumn = new TableColumn<>("สถานะคำร้องต่อไป");
        statusNextColumn.setCellValueFactory(new PropertyValueFactory<>("statusNext"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

        requestListTableView.getColumns().add(nameColumn);
        requestListTableView.getColumns().add(dateColumn);
        requestListTableView.getColumns().add(typeColumn);
        requestListTableView.getColumns().add(statusColumn);
        requestListTableView.getColumns().add(statusNextColumn);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.trim().isEmpty()) {
                search();
                dateColumn.setSortable(true);
                dateColumn.setSortType(TableColumn.SortType.DESCENDING);
                requestListTableView.getSortOrder().add(dateColumn);
                dateColumn.setSortable(false);
            } else {
                loadRequests();
                dateColumn.setSortable(true);
                dateColumn.setSortType(TableColumn.SortType.DESCENDING);
                requestListTableView.getSortOrder().add(dateColumn);
                dateColumn.setSortable(false);
            }
        });

        dateColumn.setSortType(TableColumn.SortType.DESCENDING);
        requestListTableView.getSortOrder().add(dateColumn);

        // Set the cellFactory to format the LocalDateTime
        dateColumn.setCellFactory(column -> new TextFieldTableCell<>(new StringConverter<LocalDateTime>() {
            @Override
            public String toString(LocalDateTime lastLogin) {
                return lastLogin != null ? lastLogin.format(formatter) : "";
            }

            @Override
            public LocalDateTime fromString(String string) {
                return LocalDateTime.parse(string, formatter);
            }
        }));

        nameColumn.setMinWidth(150);
        dateColumn.setMinWidth(200);
        typeColumn.setMinWidth(150);
        statusColumn.setMinWidth(190);
        statusNextColumn.setMinWidth(241);


        nameColumn.setSortable(false);
        dateColumn.setSortable(false);
        typeColumn.setSortable(false);
        statusColumn.setSortable(false);
        statusNextColumn.setSortable(false);
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

    private void search() {
        ArrayList<Request> requests = new ArrayList<>();
        for (Request request : requestList.getRequests()) {
            if (selectedStudentId.equals(request.getNisitId())){
                requests.add(request);
            }
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        Set<Request> filter = requests
                .stream()
                .filter(request -> request.getName().toLowerCase().contains(searchTextField.getText().toLowerCase()) ||
                        request.getRequestType().toLowerCase().contains(searchTextField.getText().toLowerCase()) ||
                        request.getStatusNow().contains(searchTextField.getText()) ||
                        request.getStatusNext().contains(searchTextField.getText()) ||
                        request.getDate().format(formatter).contains(searchTextField.getText()))
                .collect(Collectors.toSet());

        requestListTableView.getItems().clear();
        requestListTableView.getItems().addAll(filter);

    }
}
