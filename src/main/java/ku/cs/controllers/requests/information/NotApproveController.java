package ku.cs.controllers.requests.information;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ku.cs.controllers.advisor.AdvisorRequestsController;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.services.Datasource;
import ku.cs.services.RequestListFileDatasource;

import java.io.IOException;
import java.time.LocalDateTime;

public class NotApproveController {
    private Request request;

    @FXML
    private BorderPane borderPane;
    @FXML
    private Stage stage;
    @FXML
    private TextArea reason;
    @FXML
    private Label errorLabel;
    @FXML
    private AnchorPane anchorPane;

    @FXML
    public void initialize() {
        anchorPane.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                onAgreeClick();
            }
        });
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    @FXML
    public void onAgreeClick() {
        try {
            LocalDateTime now = LocalDateTime.now();
            Datasource<RequestList> requestListDatasource = new RequestListFileDatasource("data");
            RequestList requestList = requestListDatasource.readData();
            Request targetRequest = requestList.findByRequestUUID(request.getUuid());
            targetRequest.setStatusNow("ปฏิเสธโดยอาจารย์ที่ปรึกษา");
            targetRequest.setStatusNext("คำร้องถูกปฏิเสธ");
            targetRequest.setTimeStamp(now);
            targetRequest.setReasonForNotApprove(reason.getText());
            requestListDatasource.writeData(requestList);
            stage.close();

            String viewPath = "/ku/cs/views/advisor-requests-pane.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            AdvisorRequestsController controller = fxmlLoader.getController();
            borderPane.setCenter(pane);
            controller.setBorderPane(borderPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            errorLabel.setVisible(true);
            errorLabel.setText(e.getMessage());
        }
    }
}
