package ku.cs.controllers.faculty;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.StringConverter;
import ku.cs.models.Session;
import ku.cs.models.request.*;
import ku.cs.models.request.approver.Approver;
import ku.cs.models.request.approver.ApproverList;
import ku.cs.models.user.FacultyUser;
import ku.cs.models.user.Student;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.services.*;
import ku.cs.views.components.DefaultTableView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

public class FacultyRequestsController {
    @FXML
    TableView<Request> requestListTableView;
    @FXML
    BorderPane borderPane;
    private Datasource<RequestList> requestListDatasource;
    private Datasource<UserList> datasource;
    private RequestList requestList;
    private UserList userlist;
    private ArrayList<String> studentId;
    private Session session;
    FacultyUser loginUser;
    @FXML
    private TextField searchTextField;

    @FXML
    public void initialize() {
        studentId = new ArrayList<>();
        getStudentId();
        loadRequests();
        showTable();
        session = new Session();
        session.setUser(loginUser);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            search(newValue);
        });
        requestListTableView.getStylesheets().clear();
        requestListTableView.getStylesheets().add(getClass().getResource("/ku/cs/styles/department/pages/request-list/department-staff-request-list-table-stylesheet.css").toExternalForm());
    }

    private void search(String newValue) {
        if (requestList != null) {
            requestListTableView.getItems().clear();
            requestListTableView.getItems().addAll(requestList.getRequests()
                    .stream()
                    .filter(request -> request.getStatusNow().equals("อนุมัติโดยหัวหน้าภาควิชา") &&
                            request.getStatusNext().equals("คำร้องส่งต่อให้คณบดี") &&
                            studentId.contains(request.getNisitId()) &&
                            (request.getName().toLowerCase().contains(newValue.toLowerCase()) ||
                                    request.getRequestType().toLowerCase().contains(newValue.toLowerCase())))
                    .collect(Collectors.toList()));

            TableColumn<Request, LocalDateTime> dateColumn = (TableColumn<Request, LocalDateTime>) requestListTableView.getColumns().get(1);
            dateColumn.setSortType(TableColumn.SortType.DESCENDING);
            requestListTableView.getSortOrder().add(dateColumn);
        }
    }


    public void setLoginUser(FacultyUser loginUser) {
        if (loginUser == null) return;
        this.loginUser = loginUser;
    }

    private void showTable() {
        requestListTableView.getColumns().clear();
        Label placeHolder = new Label("ไม่พบข้อมูล");
        requestListTableView.setPlaceholder(placeHolder);
        requestListTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        requestListTableView.setOnMouseClicked(mouseEvent -> {
            Object selected = requestListTableView.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            try {
                session.setData(selected);
                FXRouter.goTo("request-management", session);
            } catch (IOException e) {
                System.err.println("error occurred while trying to go to faculty-request-manage");
            }
        });
        TableColumn<Request, String> nameColumn = new TableColumn<>("ชื่อ-นามสกุล");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Request, LocalDateTime> dateColumn = new TableColumn<>("วันที่อัปเดตล่าสุด");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("TimeStamp"));
        TableColumn<Request, String> typeColumn = new TableColumn<>("ประเภทคำร้อง");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("requestType"));
        TableColumn<Request, String> statusColumn = new TableColumn<>("สถานะคำร้อง");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("statusNow"));
        TableColumn<Request, String> statusNextColumn = new TableColumn<>("สถานะคำร้องต่อไป");
        statusNextColumn.setCellValueFactory(new PropertyValueFactory<>("statusNext"));
        RequestStatusColumn.setTableStatus(statusColumn, "now");
        RequestStatusColumn.setTableStatus(statusNextColumn, "next");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//        requestListTableView.g
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

        requestListTableView.getColumns().addAll(typeColumn, nameColumn, dateColumn, statusColumn, statusNextColumn);
    }

    private void getStudentId() {
        datasource = new UserListFileDatasource("data", "student.csv");
        userlist = datasource.readData();
        for (User user : userlist.getUsers()) {
            if (user instanceof Student) {
                Student student = (Student) user;
                if (loginUser != null && loginUser.getFaculty().equals(student.getFaculty())) {
                    studentId.add(student.getId());
                }
            }
        }
    }

    private void loadRequests() {
        if (loginUser == null) return;
        requestListDatasource = new RequestListFileDatasource("data");
        requestList = requestListDatasource.readData();
        requestListTableView.getItems().clear();

        if (requestList != null) {
            requestListTableView.getItems().addAll(requestList.getRequests()
                    .stream()
                    .filter(request -> request.getFacultyUUID().equals(loginUser.getFacultyUUID()) &&
//                            request.getStatusNow().equals("อนุมัติโดยหัวหน้าภาควิชา") &&
//                            request.getStatusNext().equals("คำร้องส่งต่อให้คณบดี") &&
                            studentId.contains(request.getNisitId()))
                    .collect(Collectors.toList()));

            TableColumn<Request, LocalDateTime> dateColumn = (TableColumn<Request, LocalDateTime>) requestListTableView.getColumns().get(1);
            dateColumn.setSortType(TableColumn.SortType.DESCENDING);
            requestListTableView.getSortOrder().add(dateColumn);
        }
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }
}
