package ku.cs.controllers.requests.information;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ku.cs.controllers.advisor.AdvisorPageController;
import ku.cs.controllers.advisor.AdvisorRequestsController;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.models.request.approver.Approver;
import ku.cs.models.request.approver.ApproverList;
import ku.cs.models.request.approver.exception.ApproverStatusException;
import ku.cs.services.*;

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
    private Label headerNotApprove;
    @FXML
    private Button agreeClickButton;
    @FXML
    private AdvisorPageController advisorPageController;

    @FXML
    public void initialize() {
        updateStyle();

        anchorPane.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                onAgreeClick();
            }
        });
    }

    public void setRequest(Request request) {
        this.request = request;
        setUpUI();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    private void setUpUI() {
        if (request.getStatusNext().equals("คำร้องถูกปฏิเสธ")) {
            agreeClickButton.setVisible(false);
            reason.setMouseTransparent(true);
            reason.setFocusTraversable(false);
            reason.setText(request.getReasonForNotApprove());
            headerNotApprove.setText("เหตุผลที่ถูกปฏิเสธคำร้อง");
            anchorPane.requestFocus();
        }
    }

    @FXML
    public void onAgreeClick() {
        try {
            LocalDateTime now = LocalDateTime.now();
            RequestListFileDatasource requestListDatasource = new RequestListFileDatasource("data");
            ApproverListFileDatasource approverListFileDatasource = new ApproverListFileDatasource("data");
            ApproverList approverList = approverListFileDatasource.readData();
            RequestList requestList = requestListDatasource.readData();
            Request targetRequest = requestList.findByRequestUUID(request.getUuid());
            Approver approver = approverList.getApproverList(targetRequest.getUuid()).getApprovers("advisor").stream().toList().getFirst();
            try {
                approver.setStatus("ไม่อนุมัติ");
            } catch (ApproverStatusException e) {
                System.out.println(e.getMessage());
            }
            requestListDatasource.appendToLog(targetRequest);
            targetRequest.setStatusNow("ปฏิเสธโดยอาจารย์ที่ปรึกษา");
            targetRequest.setStatusNext("คำร้องถูกปฏิเสธ");
            targetRequest.setTimeStamp(now);
            targetRequest.setReasonForNotApprove(reason.getText());
            requestListDatasource.writeData(requestList);
            approverListFileDatasource.writeData(approverList);
            stage.close();

            String viewPath = "/ku/cs/views/advisor-requests-pane.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            AdvisorRequestsController controller = fxmlLoader.getController();
            controller.setAdvisorPageController(advisorPageController);
            controller.initializeRequest();
            borderPane.setCenter(pane);
            controller.setBorderPane(borderPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            errorLabel.setVisible(true);
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void onExitClick() {
        stage.close();
    }

    public void updateStyle() {
        Theme.getInstance().loadCssToPage(anchorPane, new PathGenerator() {
            @Override
            public String getThemeDarkPath() {
                return getClass().getResource("/ku/cs/styles/admin-page-style-dark.css").toString();
            }
            @Override
            public String getThemeLightPath() {
                return getClass().getResource("/ku/cs/styles/admin-page-style.css").toString();
            }
        });
    }

    public void setAdvisorPageController(AdvisorPageController advisorPageController) {
        this.advisorPageController = advisorPageController;
    }
}
