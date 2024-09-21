package ku.cs.controllers.faculty;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.StringConverter;
import ku.cs.models.request.*;
import ku.cs.models.user.FacultyUser;
import ku.cs.models.user.Student;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.RequestListFileDatasource;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FacultyRequestsController {
    @FXML
    TableView requestListTableView;
    @FXML
    BorderPane borderPane;
    private Datasource<RequestList> requestListDatasource;
    private Datasource<UserList> datasource;
    private RequestList requestList;
    private UserList userlist;
    private ArrayList<String> studentId;
    FacultyUser loginUser;

    @FXML
    public void initialize() {
        studentId = new ArrayList<>();
        getStudentId();
        loadRequests();
        showTable();
    }


    public void setLoginUser(FacultyUser loginUser){
        if (loginUser == null) {return;}
        this.loginUser = loginUser;
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH:mm:ss");

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

        requestListTableView.getColumns().add(nameColumn);
        requestListTableView.getColumns().add(dateColumn);
        requestListTableView.getColumns().add(typeColumn);
        requestListTableView.getColumns().add(statusColumn);
        requestListTableView.getColumns().add(statusNextColumn);
    }

    private void getStudentId(){
        datasource = new UserListFileDatasource("data", "student.csv");
        userlist = datasource.readData();
        for (User user : userlist.getUsers()){
            if (user instanceof Student){
                Student student = (Student) user;
                if (loginUser != null && loginUser.getFaculty().equals(student.getFaculty())) {
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
                if (request.getStatusNow().equals("อนุมัติโดยหัวหน้าภาควิชา") && request.getStatusNext().equals("คำร้องส่งต่อให้คณบดี") && studentId.contains(request.getNisitId())){
                    requestListTableView.getItems().add(request);
                }
            }
            TableColumn<Request, LocalDateTime> dateColumn = (TableColumn<Request, LocalDateTime>) requestListTableView.getColumns().get(1);
            dateColumn.setSortType(TableColumn.SortType.DESCENDING);
            requestListTableView.getSortOrder().add(dateColumn);
        }
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }
}
