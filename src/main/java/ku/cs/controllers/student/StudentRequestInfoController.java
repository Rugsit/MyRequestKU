package ku.cs.controllers.student;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.StringConverter;
import ku.cs.controllers.requests.information.*;
import ku.cs.models.request.*;
import ku.cs.models.user.Student;
import ku.cs.services.RequestListFileDatasource;
import ku.cs.views.components.DefaultTableView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class StudentRequestInfoController {
    private Student loginUser;
    private Request request;
    private DefaultTableView<Request> tableView;
    private RequestListFileDatasource datasource;
    @FXML TableView requestLogTableView;
    @FXML Label requestTypeLabel;
    @FXML Label createdDateLabel;
    @FXML Label requestNumberLabel;
    @FXML Button requestStatus1;
    @FXML Button requestStatus2;
    @FXML BorderPane borderPane;
    @FXML ImageView statusIconImageView;

    @FXML
    public void initialize() {
        tableView = new DefaultTableView<>(requestLogTableView);
        datasource = new RequestListFileDatasource("data");
    }

    public void showInfo(){
        requestTypeLabel.setText(request.getRequestType());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        createdDateLabel.setText(request.getDate().format(formatter));
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
        date.setSortType(TableColumn.SortType.DESCENDING);
        requestLogTableView.getColumns().add(date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

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

        tableView.getTableView().getSortOrder().add(date);
        tableView.addStyleSheet("/ku/cs/styles/admin-page-style.css");
        tableView.addColumn("สถานะคำร้อง", "statusNow");
        tableView.addColumn("", "statusNext");
        RequestList requestList = datasource.queryLog(request);
        for (Request req : requestList.getRequests()) {
            tableView.getTableView().getItems().add(req);
        }
        tableView.getTableView().sort();
    }

    public void onBackButtonClick() {
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

    public void setLoginUser(Student loginUser) {
        this.loginUser = loginUser;
    }

    public void setRequest(Request request) {
        this.request = request;
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
            controller.setBackPage("student");
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

}
