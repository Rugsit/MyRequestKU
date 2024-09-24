package ku.cs.controllers.advisor;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.StringConverter;
import ku.cs.controllers.admin.AdminProfileController;
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
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AdvisorRequestsController {
    @FXML
    private TableView requestListTableView;
    @FXML
    private BorderPane borderPane;
    @FXML
    private TextField searchTextField;

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
        setTableStatus(statusColumn, "now");
        setTableStatus(statusNextColumn, "next");
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

        requestListTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Student student = (Student) userlist.findUserByUUID(((Request) newValue).getOwnerUUID());
                goToInformationPage((Request) newValue, student);
            }
        });

//        nameColumn.setMinWidth(150);
//        dateColumn.setMinWidth(200);
//        typeColumn.setMinWidth(150);
//        statusColumn.setMinWidth(190);
//        statusNextColumn.setMinWidth(241);

        dateColumn.setSortable(false);
        nameColumn.setSortable(false);
        typeColumn.setSortable(false);
        statusColumn.setSortable(false);
        statusNextColumn.setSortable(false);
    }

    private void search() {
        ArrayList<Request> requests = new ArrayList<>();
        for (Request request : requestList.getRequests()) {
            if (studentId.contains(request.getNisitId())){
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
                if (studentId.contains(request.getNisitId()) && request.getStatusNext().equals("คำร้องส่งต่อให้อาจารย์ที่ปรึกษา")){
                    requestListTableView.getItems().add(request);
                }
            }
        }
    }


    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    private void goToInformationPage(Request request, Student student) {
        try {
            String viewPath = "/ku/cs/views/main-information.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            MainInformationController controller = fxmlLoader.getController();
            controller.setLoginUser(student);
            controller.setRequest(request);
            controller.setBorderPane(borderPane);
            controller.setBackPage("advisorRequest");
            controller.initializeMainInformation();
            if (request instanceof GeneralRequestForm) {
                controller.setTitleLabel("ใบคำร้องทั่วไป");
            } else if (request instanceof RegisterRequestForm) {
                controller.setTitleLabel("คำร้องขอลงทะเบียน");
            } else if (request instanceof AcademicLeaveRequestForm) {
                controller.setTitleLabel("ใบคำร้องขอลาพักการศึกษา");
            } else if (request instanceof Ku1AndKu3RequestForm) {
                if (request.getRequestType().equalsIgnoreCase("KU1")) {
                    controller.setTitleLabel("แบบลงทะเบียนเรียน KU1");
                } else {
                    controller.setTitleLabel("แบบขอเปลี่ยนแปลงการลงทะเบียนเรียน KU3");
                }
            }
            borderPane.setCenter(pane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setTableStatus(TableColumn<Request, String> status, String whichStatus) {
        status.setMaxWidth(250);
        status.setSortable(false);
        status.setCellFactory(c -> new TableCell<Request, String>() {
            Request request;
            Button statusCell = new Button();

            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                statusCell.setMouseTransparent(true);
                statusCell.setFocusTraversable(false);
                String buttonStyle = "-fx-border-radius: 3;" +
                        "-fx-font-size: 18;" +
                        "-fx-text-alignment: center;" +
                        "-fx-cursor: none;" +
                        "-fx-pref-width: 234;" +
                        "-fx-pref-height: 35;";
                if (empty || getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                } else {
                    request = getTableView().getItems().get(getIndex());
                    if (whichStatus.equals("now")) {
                        statusCell.setText(request.getStatusNow());
                    } else if (whichStatus.equals("next")) {
                        statusCell.setText(request.getStatusNext());
                    }
                    setGraphic(statusCell);
                    statusCell.setStyle("-fx-background-color: #F2FFF7;" +
                            "-fx-border-color: #00B448;" +
                            "-fx-text-fill: #00B448;" +
                            buttonStyle);
                    Pattern rejected = Pattern.compile(".*ปฏิเสธ.*");
                    Pattern newlyCreated = Pattern.compile(".*ใหม่.*");
                    Pattern inProgress = Pattern.compile(".*ต่อ.*");
                    if (newlyCreated.matcher(statusCell.getText()).matches()) {
                        statusCell.setStyle(
                                "-fx-background-color: #EBEEFF; " +
                                        "-fx-border-color: #4E7FFF; " +
                                        "-fx-text-fill: #4E7FFF;" +
                                        buttonStyle
                        );
                    }
                    if (rejected.matcher(statusCell.getText()).matches()) {
                        statusCell.setStyle(
                                "-fx-background-color: #FFDEDE; " +
                                        "-fx-border-color: #FE6463; " +
                                        "-fx-text-fill: #FE6463;" +
                                        buttonStyle
                        );
                    }
                    if (inProgress.matcher(statusCell.getText()).matches()) {
                        statusCell.setStyle(
                                "-fx-background-color: #FFF6E8; " +
                                        "-fx-border-color: #ED9B22; " +
                                        "-fx-text-fill: #ED9B22;" +
                                        buttonStyle
                        );

                    }

                    if (rejected.matcher(statusCell.getText()).matches()) {
                        statusCell.setStyle(
                                "-fx-background-color: #FFDEDE; " +
                                        "-fx-border-color: #FE6463; " +
                                        "-fx-text-fill: #FE6463;" +
                                        buttonStyle
                        );
                    }
                }
            }
        });
    }
}
