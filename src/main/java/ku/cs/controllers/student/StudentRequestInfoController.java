package ku.cs.controllers.student;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.models.user.Student;
import ku.cs.services.RequestListFileDatasource;
import ku.cs.views.components.DefaultTableView;

import java.io.IOException;
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

    @FXML
    public void initialize() {
        tableView = new DefaultTableView<>(requestLogTableView);
        datasource = new RequestListFileDatasource("data");
    }

    void showInfo(){
        requestTypeLabel.setText(request.getRequestType());
        createdDateLabel.setText(request.getDate().toString());
        requestNumberLabel.setText(request.getUuid().toString());
        String status1 = request.getStatusNow(), status2 = request.getStatusNext();
        Pattern rejected = Pattern.compile(".*ปฏิเสธ.*");
        Pattern newlyCreated = Pattern.compile(".*ใหม่.*");
        Pattern inProgress = Pattern.compile(".*ต่อ.*");
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

        }

        requestStatus1.setText(request.getStatusNow());
        requestStatus2.setText(request.getStatusNext());
    }

    void showTable() {
        tableView.getTableView().getColumns().clear();
        tableView.getTableView().getStylesheets().clear();
        TableColumn<Request, String> date = new TableColumn<>("วันที่และเวลา");
        date.setCellValueFactory(new PropertyValueFactory<>("TimeStamp"));
        date.setSortType(TableColumn.SortType.DESCENDING);
        requestLogTableView.getColumns().add(date);
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

    void setLoginUser(Student loginUser) {
        this.loginUser = loginUser;
    }

    void setRequest(Request request) {
        this.request = request;
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

}
