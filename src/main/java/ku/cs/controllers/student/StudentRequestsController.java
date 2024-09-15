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
import ku.cs.controllers.requests.ChooseRequestFromController;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.models.user.Student;
import ku.cs.models.user.User;
import ku.cs.services.Datasource;
import ku.cs.services.RequestListFileDatasource;

import java.io.IOException;


public class StudentRequestsController {
    @FXML Label requestsNumberLabel;
    @FXML Label approvedNumberLabel;
    @FXML Label rejectedNumberLabel;
    @FXML TableView requestListTableView;
    @FXML BorderPane borderPane;
    @FXML
    Button createRequestFormButton;

    private Datasource<RequestList> requestListDatasource;
    private RequestList requestList;
    private RequestList myRequests;
    Student loginUser;

    // TODO: fetch data from datasource instead
    public void initialize() {
        showTable();
        showInfo();
    }

    private void showTable(){
        requestListTableView.getColumns().clear();
        TableColumn<Request, String> typeColumn = new TableColumn<>("ประเภทคำร้อง");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("requestType"));
        TableColumn<Request, String> createColumn = new TableColumn<>("วันที่ยื่นคำร้อง");
        createColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));
        TableColumn<Request, String> latestColumn = new TableColumn<>("วันที่อัพเดทล่าสุด");
        latestColumn.setCellValueFactory(new PropertyValueFactory<>("TimeStamp"));
        TableColumn<Request, String> statusColumn = new TableColumn<>("สถานะคำร้อง");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("statusNow"));
        requestListTableView.getColumns().add(typeColumn);
        requestListTableView.getColumns().add(createColumn);
        requestListTableView.getColumns().add(latestColumn);
        requestListTableView.getColumns().add(statusColumn);
        typeColumn.setMinWidth(150);
        createColumn.setMinWidth(200);
        latestColumn.setMinWidth(200);
        statusColumn.setMinWidth(381);
        if (loginUser == null) {return;}
        requestListDatasource = new RequestListFileDatasource("data");
        requestList = requestListDatasource.readData();
        myRequests = new RequestList();
        for (Request request : requestList.getRequests()) {
            System.out.println("find");
            if (request.getOwnerUUID().equals(loginUser.getUUID())) {
            System.out.println("found");
                myRequests.addRequest(request);
                requestListTableView.getItems().add(request);
            }
        }
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
            String viewPath = "/ku/cs/views/choose-request-form-pane.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            ChooseRequestFromController controller = fxmlLoader.getController();
            controller.setBorderPane(this.borderPane);
            borderPane.setCenter(pane);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    void setLoginUser(Student loginUser) {
        if (loginUser == null) {return;}
        this.loginUser = loginUser;
    }
}
