package ku.cs.controllers.student;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import ku.cs.controllers.advisor.AdvisorPageController;
import ku.cs.controllers.advisor.AdvisorStudentRequestsController;
import ku.cs.controllers.requests.information.*;
import ku.cs.models.request.*;
import ku.cs.models.request.approver.Approver;
import ku.cs.models.user.Student;
import ku.cs.services.*;
import ku.cs.views.components.DefaultTableView;
import ku.cs.views.components.PDFViewPopup;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class StudentRequestInfoController {
    private Student loginUser;
    private Request request;
    private DefaultTableView<Request> tableView;
    private RequestListFileDatasource datasource;
    private DateTimeFormatter dateTimeFormatter;
    private String backPage;

    @FXML TableView requestLogTableView;
    @FXML Label requestTypeLabel;
    @FXML Label createdDateLabel;
    @FXML Label requestNumberLabel;
    @FXML Button requestStatus1;
    @FXML Button requestStatus2;
    @FXML BorderPane borderPane;
    @FXML ImageView statusIconImageView;
    @FXML
    private Stage currentNotApprove;
    @FXML
    private HBox buttonHbox;
    @FXML
    private Button seeReject;
    @FXML
    private Button seeInformationButton;
    @FXML
    private Button backButton;
    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private ChoiceBox<Approver> approverChoiceBox;
    @FXML
    private StackPane mainStackPane;
    @FXML
    private Button showPDFButton;
    @FXML
    private VBox approverPDFVbox;

    private AdvisorPageController advisorPageController;

    public void setAdvisorPageController(AdvisorPageController advisorPageController) {
        this.advisorPageController = advisorPageController;
    }

    @FXML
    public void initialize() {
        SetTransition.setButtonBounce(seeInformationButton);
        SetTransition.setButtonBounce(backButton);
        tableView = new DefaultTableView<>(requestLogTableView);
        datasource = new RequestListFileDatasource("data");
        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
    }

    public void showInfo(){
        if (request.getStatusNext().equals("คำร้องถูกปฏิเสธ")) {
            seeReject.setVisible(true);
            HBox.setMargin(seeReject, new Insets(0, 0, 0, 20));
            buttonHbox.getChildren().add(seeReject);
        }
        requestTypeLabel.setText(request.getRequestType());
        createdDateLabel.setText(request.getDate().format(dateTimeFormatter));
        requestNumberLabel.setText(request.getUuid().toString());
        String status1 = request.getStatusNow(), status2 = request.getStatusNext();
        Pattern rejected = Pattern.compile(".*ปฏิเสธ.*");
        Pattern newlyCreated = Pattern.compile(".*ใหม่.*");
        Pattern inProgress = Pattern.compile(".*ต่อ.*");
        Pattern completed = Pattern.compile(".*ครบถ้วน.*");
        if (newlyCreated.matcher(status1).matches()){
            requestStatus1.setStyle(
                    "-fx-background-color: #EBEEFF; " +
                            "-fx-border-color: #4E7FFF; " +
                            "-fx-text-fill: #4E7FFF;"
            );

            requestStatus2.setStyle(
                    "-fx-background-color: #FFF6E8;" +
                            " -fx-border-color: #ED9B22;" +
                            " -fx-text-fill: #ED9B22;"
            );

        }
        if (inProgress.matcher(status2).matches()){
            requestStatus2.setStyle(
                    "-fx-background-color: #FFF6E8; " +
                            "-fx-border-color: #ED9B22; " +
                            "-fx-text-fill: #ED9B22;"
            );

        }

        if (rejected.matcher(status1).matches()){
            requestStatus1.setStyle(
                    "-fx-background-color: #FFDEDE; " +
                            "-fx-border-color: #FE6463; " +
                            "-fx-text-fill: #FE6463;"
            );

            requestStatus2.setStyle(
                    "-fx-background-color: #FFDEDE; " +
                            "-fx-border-color: #FE6463; " +
                            "-fx-text-fill: #FE6463;"
            );
            Image statusIcon = new Image(getClass().getResourceAsStream("/images/icons/request-status-rejected.png"));
            statusIconImageView.setImage(statusIcon);

        }

        if (completed.matcher(status2).matches()){
            Image statusIcon = new Image(getClass().getResourceAsStream("/images/icons/request-status-approved.png"));
            statusIconImageView.setImage(statusIcon);
        }

        requestStatus1.setText(request.getStatusNow());
        requestStatus2.setText(request.getStatusNext());
    }

    public void showTable() {
        tableView.getTableView().getColumns().clear();
        tableView.getTableView().getStylesheets().clear();
        TableColumn<Request, LocalDateTime> date = new TableColumn<>("วันที่และเวลา");
        date.setCellValueFactory(new PropertyValueFactory<>("TimeStamp"));
        setTableDateTime(date);
        TableColumn<Request, String> statusNow = new TableColumn<>("สถานะคำร้อง");
        statusNow.setCellValueFactory(new PropertyValueFactory<>("statusNow"));
        TableColumn<Request, String> statusNext = new TableColumn<>("");
        statusNext.setCellValueFactory(new PropertyValueFactory<>("statusNext"));
        date.setSortType(TableColumn.SortType.DESCENDING);
        requestLogTableView.getColumns().add(date);
        requestLogTableView.getColumns().add(statusNow);
        requestLogTableView.getColumns().add(statusNext);
        date.setMaxWidth(300);

        RequestStatusColumn.setTableStatus(statusNow, "now");
        RequestStatusColumn.setTableStatus(statusNext, "next");
        tableView.getTableView().getSortOrder().add(date);

        RequestList requestList = datasource.queryLog(request);
        for (Request req : requestList.getRequests()) {
            tableView.getTableView().getItems().add(req);
        }
        tableView.getTableView().sort();
        date.setSortable(false);
    }

    public void onBackButtonClick() {
        if (backPage != null && backPage.equalsIgnoreCase("advisorStudentRequest")) {
            goToAdvisorPage();
        } else {
            try {
                String viewPath = "/ku/cs/views/student-requests-pane.fxml";
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource(viewPath));
                Pane pane = fxmlLoader.load();
                StudentRequestsController controller = fxmlLoader.getController();
                controller.setLoginUser(loginUser);
                controller.initialize();
                borderPane.setCenter(pane);
                controller.setBorderPane(borderPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setLoginUser(Student loginUser) {
        this.loginUser = loginUser;
    }

    private void setTableDateTime(TableColumn<Request, LocalDateTime> date) {
        date.setCellFactory(column -> new TextFieldTableCell<>(new StringConverter<LocalDateTime>() {
            @Override
            public String toString(LocalDateTime lastLogin) {
                return lastLogin != null ? lastLogin.format(dateTimeFormatter) : "";
            }

            @Override
            public LocalDateTime fromString(String string) {
                return LocalDateTime.parse(string, dateTimeFormatter);
            }
        }));
    }

    public void setRequest(Request request) {
        this.request = request;
        initApproverChoose();
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    @FXML
    private void seeInformation() {
        goToInformationPage();
    }

    private void goToInformationPage() {
        try {
            String viewPath = "/ku/cs/views/main-information.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            MainInformationController controller = fxmlLoader.getController();
            controller.setLoginUser(loginUser);
            controller.setRequest(request);
            controller.setBorderPane(borderPane);
            controller.setBackPage(backPage);
            controller.setAdvisorPageController(advisorPageController);
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

    @FXML
    private void seeRejectReason() {
        if (currentNotApprove == null || !currentNotApprove.isShowing()) {
            currentNotApprove = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/not-approve-popup.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            NotApproveController notApproveController = fxmlLoader.getController();
            notApproveController.setRequest(request);
            notApproveController.setStage(currentNotApprove);
            notApproveController.setBorderPane(borderPane);
            currentNotApprove.setScene(scene);
            currentNotApprove.initModality(Modality.APPLICATION_MODAL);
            currentNotApprove.setTitle("Not Approve");
            currentNotApprove.show();
        }
    }

    private void goToAdvisorPage() {
        try {
            String viewPath = "/ku/cs/views/advisor-student-requests-pane.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            AdvisorStudentRequestsController controller = fxmlLoader.getController();

            if (loginUser instanceof Student) {
                Student selectedStudent = loginUser;
                String  selectedStudentId = selectedStudent.getId();

                controller.setSelectedStudentId(selectedStudentId);
                controller.initializeStudentRequests();
                controller.setStudentName(selectedStudent.getName());
                controller.setAdvisorPageController(advisorPageController);
            }

            borderPane.setCenter(pane);
            controller.setBorderPane(borderPane);
            controller.setStudent(loginUser);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setBackPage(String backPage) {
        this.backPage = backPage;
    }

    public void setSeeInformationVisible(boolean status) {
        seeInformationButton.setVisible(status);
    }

    public void setBackButtonVisible(boolean status) {
        backButton.setVisible(status);
        approverPDFVbox.setVisible(status);
        if (!backButton.isVisible()) {
            updateStyle();
        }
    }

    public void updateStyle() {
        Theme.getInstance().loadCssToPage(mainAnchorPane, new PathGenerator() {
            @Override
            public String getThemeDarkPath() {
                return getClass().getResource("/ku/cs/styles/general-dark.css").toString();
            }
            @Override
            public String getThemeLightPath() {
                return getClass().getResource("/ku/cs/styles/general.css").toString();
            }
        });
    }

    private void initApproverChoose() {
        approverChoiceBox.getItems().clear();
        approverChoiceBox.getItems().addAll(request.getApproverList().getApprovers());
        approverChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Approver object) {
                if (object != null) {
                    return object.getName() + " (" + object.getRole() + ")";
                } else {
                    return "";
                }

            }

            @Override
            public Approver fromString(String string) {
                return null;
            }
        });

        approverChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.getSignatureFile().equalsIgnoreCase("no-image")) {
                    showPDFButton.setDisable(true);
                } else {
                    showPDFButton.setDisable(false);
                }
            }
        });
    }

    @FXML
    private void showPDF() {
        Approver approver = approverChoiceBox.getValue();
        try {
            PDFDatasource datasource = new PDFDatasource();
            File pdfFile = datasource.getPDFFile(approver.getSignatureFile());
            if(pdfFile!=null){
                mainStackPane.getChildren().add(new PDFViewPopup(pdfFile){
                    @Override
                    protected void initPopupView(){
                        super.initPopupView();
                        titleLabel.changeText("ลายเซ็น");
                    }
                    @Override
                    protected void handleFirstButton(){
                        firstButton.setOnMouseClicked(e -> {
                            datasource.downloadFile(approver.getSignatureFile());
                        });
                    }
                    @Override
                    protected void handleSecondButton(){
                        secondButton.setOnMouseClicked(e ->{
                            mainStackPane.getChildren().removeLast();
                            mainStackPane.setVisible(false);
                        });
                    }
                });
                mainStackPane.setVisible(true);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
