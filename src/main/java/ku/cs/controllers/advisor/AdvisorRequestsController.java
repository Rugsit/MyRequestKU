package ku.cs.controllers.advisor;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.StringConverter;
import ku.cs.controllers.requests.information.*;
import ku.cs.controllers.student.StudentRequestInfoController;
import ku.cs.models.request.*;
import ku.cs.models.user.Student;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.services.Datasource;
import ku.cs.services.RequestListFileDatasource;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

        requestListTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Student student = (Student) userlist.findUserByUUID(((Request) newValue).getOwnerUUID());
                try {
                    if (newValue instanceof GeneralRequestForm) {
                        showGeneralInformation((Request) newValue, student);
                    } else if (newValue instanceof RegisterRequestForm) {
                        showRegisterInformation((Request) newValue, student);
                    } else if (newValue instanceof AcademicLeaveRequestForm) {
                        showAcademicInformation((Request) newValue, student);
                    } else if (newValue instanceof Ku1AndKu3RequestForm) {
                        if (((Request) newValue).getRequestType().equalsIgnoreCase("KU1")) {
                            showKU1Information((Request) newValue, student);
                        } else {
                            showKU3Information((Request) newValue, student);
                        }
                    }
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }
        });

//        nameColumn.setMinWidth(150);
//        dateColumn.setMinWidth(200);
//        typeColumn.setMinWidth(150);
//        statusColumn.setMinWidth(190);
//        statusNextColumn.setMinWidth(241);

        requestListTableView.getColumns().add(nameColumn);
        requestListTableView.getColumns().add(dateColumn);
        requestListTableView.getColumns().add(typeColumn);
        requestListTableView.getColumns().add(statusColumn);
        requestListTableView.getColumns().add(statusNextColumn);
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
            TableColumn<Request, LocalDateTime> dateColumn = (TableColumn<Request, LocalDateTime>) requestListTableView.getColumns().get(1);
            dateColumn.setSortType(TableColumn.SortType.DESCENDING);
            requestListTableView.getSortOrder().add(dateColumn);
        }
    }


    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    private void showAcademicInformation(Request request, Student student) throws IOException{
        String viewPath = "/ku/cs/views/academic-leave-form-information.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(viewPath));
        Pane pane = fxmlLoader.load();
        AcademicLeaveInformationRequestFormController controller = fxmlLoader.getController();
        controller.setLoginUser(student);
        controller.setRequest(request);
        controller.setBackPage("advisorRequest");
        borderPane.setCenter(pane);
        controller.setBorderPane(borderPane);
        controller.showData();
    }
    private void showGeneralInformation(Request request, Student student) throws IOException{
        String viewPath = "/ku/cs/views/general-request-form-information.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(viewPath));
        Pane pane = fxmlLoader.load();
        GeneralInformaitonRequestFormController controller = fxmlLoader.getController();
        controller.setLoginUser(student);
        controller.setRequest(request);
        controller.setBackPage("advisorRequest");
        borderPane.setCenter(pane);
        controller.setBorderPane(borderPane);
        controller.showData();
    }
    private void showRegisterInformation(Request request, Student student) throws IOException{
        String viewPath = "/ku/cs/views/register-request-form-information.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(viewPath));
        Pane pane = fxmlLoader.load();
        RegisterInformationRequestFormController controller = fxmlLoader.getController();
        controller.setLoginUser(student);
        controller.setRequest(request);
        controller.setBackPage("advisorRequest");
        borderPane.setCenter(pane);
        controller.setBorderPane(borderPane);
        controller.showData();
    }
    private void showKU1Information(Request request, Student student) throws IOException{
        String viewPath = "/ku/cs/views/ku1-form-information.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(viewPath));
        Pane pane = fxmlLoader.load();
        Ku1InformationRequestFormController controller = fxmlLoader.getController();
        controller.setLoginUser(student);
        controller.setRequest(request);
        controller.setBackPage("advisorRequest");
        borderPane.setCenter(pane);
        controller.setBorderPane(borderPane);
        controller.showData();
    }
    private void showKU3Information(Request request, Student student) throws IOException{
        String viewPath = "/ku/cs/views/ku3-form-information.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(viewPath));
        Pane pane = fxmlLoader.load();
        Ku3InformationRequestFormController controller = fxmlLoader.getController();
        controller.setLoginUser(student);
        controller.setRequest(request);
        controller.setBackPage("advisorRequest");
        borderPane.setCenter(pane);
        controller.setBorderPane(borderPane);
        controller.showData();
    }

}
