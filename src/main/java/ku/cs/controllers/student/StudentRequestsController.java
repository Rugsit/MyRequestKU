package ku.cs.controllers.student;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import ku.cs.controllers.requests.ChooseRequestFromController;
import ku.cs.controllers.requests.ErrorGeneralRequestFormController;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.models.user.Student;
import ku.cs.services.Datasource;
import ku.cs.services.RequestListFileDatasource;
import ku.cs.views.components.DefaultTableView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class StudentRequestsController {
    @FXML
    private Stage currentErrorStage;
    @FXML Label requestsNumberLabel;
    @FXML Label approvedNumberLabel;
    @FXML Label rejectedNumberLabel;
    @FXML TableView requestListTableView;
    @FXML BorderPane borderPane;
    @FXML
    Button createRequestFormButton;
    @FXML
    TextField searchTextField;

    private Datasource<RequestList> requestListDatasource;
    private RequestList requestList;
    private RequestList myRequests;
    Student loginUser;
    DefaultTableView<Request> requestListTable;

    // TODO: fetch data from datasource instead
    public void initialize() {
        showTable();
        showInfo();

    }

    public void showTable(){
        requestListTable = new DefaultTableView<>(requestListTableView) {
            @Override
            protected void handleCLick() {
                getTableView().setOnMouseClicked(e-> {
                    Object selected = getTableView().getSelectionModel().getSelectedItem();
                    if(selected != null){
                        try {
                            String viewPath = "/ku/cs/views/student-request-info-pane.fxml";
                            FXMLLoader fxmlLoader = new FXMLLoader();
                            fxmlLoader.setLocation(getClass().getResource(viewPath));
                            Pane pane = fxmlLoader.load();
                            StudentRequestInfoController controller = fxmlLoader.getController();
                            controller.setLoginUser(loginUser);
                            controller.setRequest((Request) selected);
                            controller.showInfo();
                            controller.showTable();
                            controller.setBorderPane(borderPane);
                            borderPane.setCenter(pane);
                        } catch (IOException exception) {
                            System.err.println("Error: handle click");
                        }
                    }
                });
            }
        };
        requestListTable.getTableView().getStylesheets().clear();
        requestListTable.addStyleSheet("/ku/cs/styles/admin-page-style.css");
        requestListTableView.getColumns().clear();
        Label placeHolder = new Label("ไม่พบข้อมูล");
        requestListTableView.setPlaceholder(placeHolder);
        requestListTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        requestListTable.addColumn("ประเภทคำร้อง", "requestType");
        requestListTable.addColumn("วันที่ยื่นคำร้อง", "Date");
        requestListTable.addColumn("วันที่อัพเดทล่าสุด", "TimeStamp");
        TableColumn<Request, String> statusNow = new TableColumn<>("สถานะคำร้อง");
        statusNow.setCellValueFactory(new PropertyValueFactory<>("statusNow"));
        TableColumn<Request, String> statusNext = new TableColumn<>("");
        statusNext.setCellValueFactory(new PropertyValueFactory<>("statusNext"));
        setTableStatus(statusNow, "now");
        setTableStatus(statusNext, "next");
        requestListTable.getTableView().getColumns().add(statusNow);
        requestListTable.getTableView().getColumns().add(statusNext);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

        TableColumn<Request, LocalDateTime> date = (TableColumn<Request, LocalDateTime>) requestListTable.getTableView().getColumns().get(1);
        date.setCellFactory(column -> new TextFieldTableCell<>(new StringConverter<LocalDateTime>() {
            @Override
            public String toString(LocalDateTime lastLogin) {
                return lastLogin != null ? lastLogin.format(formatter) : "";
            }

            @Override
            public LocalDateTime fromString(String string) {
                return LocalDateTime.parse(string, formatter);
            }
        }));

        TableColumn<Request, LocalDateTime> timestamp = (TableColumn<Request, LocalDateTime>) requestListTable.getTableView().getColumns().get(2);
        timestamp.setCellFactory(column -> new TextFieldTableCell<>(new StringConverter<LocalDateTime>() {
            @Override
            public String toString(LocalDateTime lastLogin) {
                return lastLogin != null ? lastLogin.format(formatter) : "";
            }

            @Override
            public LocalDateTime fromString(String string) {
                return LocalDateTime.parse(string, formatter);
            }
        }));

        if (loginUser == null) {return;}
        requestListDatasource = new RequestListFileDatasource("data");
        requestList = requestListDatasource.readData();
        myRequests = new RequestList();
        for (Request request : requestList.getRequests()) {
            if (request.getOwnerUUID().equals(loginUser.getUUID())) {
                myRequests.addRequest(request);
                requestListTable.getTableView().getItems().add(request);
            }
        }

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.trim().isEmpty()) {
                search();
                timestamp.setSortable(true);
                timestamp.setSortType(TableColumn.SortType.DESCENDING);
                requestListTableView.getSortOrder().add(timestamp);
                timestamp.setSortable(false);
            } else {
                requestListTableView.getItems().clear();
                myRequests = new RequestList();
                for (Request request : requestList.getRequests()) {
                    if (request.getOwnerUUID().equals(loginUser.getUUID())) {
                        myRequests.addRequest(request);
                        requestListTable.getTableView().getItems().add(request);
                    }
                }
                timestamp.setSortable(true);
                timestamp.setSortType(TableColumn.SortType.DESCENDING);
                requestListTableView.getSortOrder().add(timestamp);
                timestamp.setSortable(false);
            }
        });
        timestamp.setSortable(true);
        timestamp.setSortType(TableColumn.SortType.DESCENDING);
        requestListTableView.getSortOrder().add(timestamp);
        timestamp.setSortable(false);
    }

    // TODO: fetch data from datasource instead
    private void showInfo(){
        if (myRequests == null) {
            requestsNumberLabel.setText("0");
        } else{
            requestsNumberLabel.setText("" + myRequests.getRequests().size());
        }
        approvedNumberLabel.setText("0");
        rejectedNumberLabel.setText("0");
    }

    @FXML
    public void onCreateFromClick() {
        try {
            if (loginUser.getAdvisor() == null) {
                throw new IllegalArgumentException("คุณยังไม่มีอาจารย์ที่ปรึกษาไม่สามารถสร้าง ใบคำร้องได้");
            }
            String viewPath = "/ku/cs/views/choose-request-form-pane.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            ChooseRequestFromController controller = fxmlLoader.getController();
            controller.setBorderPane(this.borderPane);
            borderPane.setCenter(pane);

        } catch (IOException | IllegalArgumentException e) {
            try {
                if (currentErrorStage == null || !currentErrorStage.isShowing()) {
                    currentErrorStage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/error-page.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    ErrorGeneralRequestFormController errorGeneralRequestFormController = fxmlLoader.getController();
                    errorGeneralRequestFormController.setErrorMessage(e.getMessage());
                    ErrorGeneralRequestFormController controller = fxmlLoader.getController();
                    controller.setStage(this.currentErrorStage);
                    scene.getStylesheets().add(getClass().getResource("/ku/cs/styles/error-confirm-edit-page-style.css").toExternalForm());
                    currentErrorStage.setScene(scene);
                    currentErrorStage.initModality(Modality.APPLICATION_MODAL);
                    currentErrorStage.setTitle("Error");
                    currentErrorStage.show();
                }
            } catch (IOException ee) {
                System.err.println("Error: " + ee.getMessage());
            }
        }
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    public void setLoginUser(Student loginUser) {
        if (loginUser == null) {return;}
        this.loginUser = loginUser;
    }

    private void setTableStatus(TableColumn<Request, String> status, String whichStatus) {
        status.setMaxWidth(250);
        status.setSortable(false);
        status.setCellFactory(c -> new TableCell<Request, String>() {
            Request request;
            Button statusCell = new Button();
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
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
                    if (newlyCreated.matcher(statusCell.getText()).matches()){
                        statusCell.setStyle(
                                "-fx-background-color: #EBEEFF; " +
                                        "-fx-border-color: #4E7FFF; " +
                                        "-fx-text-fill: #4E7FFF;"+
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
                    if (inProgress.matcher(statusCell.getText()).matches()){
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


    private void search() {
        myRequests = new RequestList();
        for (Request request : requestList.getRequests()) {
            if (request.getOwnerUUID().equals(loginUser.getUUID())) {
                myRequests.addRequest(request);
                requestListTable.getTableView().getItems().add(request);
            }
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        Set<Request> filter = myRequests.getRequests()
                .stream()
                .filter(request -> request.getRequestType().toLowerCase().contains(searchTextField.getText().toLowerCase()) ||
                        request.getStatusNow().contains(searchTextField.getText()) ||
                        request.getStatusNext().contains(searchTextField.getText()))
                .collect(Collectors.toSet());

        requestListTableView.getItems().clear();
        requestListTableView.getItems().addAll(filter);
    }
}
