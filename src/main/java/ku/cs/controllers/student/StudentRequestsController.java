package ku.cs.controllers.student;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.controllers.requests.ChooseRequestFromController;
import ku.cs.controllers.requests.ErrorGeneralRequestFormController;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.models.user.Student;
import ku.cs.services.Datasource;
import ku.cs.services.RequestListFileDatasource;
import ku.cs.views.components.DefaultTableView;

import java.io.IOException;


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
        requestListTable.addColumn("สถานะคำร้อง", "statusNow");
        requestListTable.addColumn("", "statusNext");

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
                throw new IllegalArgumentException("คุณยังไม่มีอาจารที่ปรึกษาไม่สามารถสร้างใบคำร้องได้");
            }
        } catch (IllegalArgumentException e) {
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

    public void setLoginUser(Student loginUser) {
        if (loginUser == null) {return;}
        this.loginUser = loginUser;
    }
}
