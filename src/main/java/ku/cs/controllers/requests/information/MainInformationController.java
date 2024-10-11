package ku.cs.controllers.requests.information;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.controllers.advisor.AdvisorPageController;
import ku.cs.controllers.advisor.AdvisorRequestsController;
import ku.cs.controllers.student.StudentRequestInfoController;
import ku.cs.models.request.*;
import ku.cs.models.request.approver.Approver;
import ku.cs.models.request.approver.ApproverList;
import ku.cs.models.request.approver.exception.ApproverStatusException;
import ku.cs.models.user.Student;
import ku.cs.models.user.User;
import ku.cs.services.*;

import java.io.IOException;
import java.time.LocalDateTime;

public class MainInformationController {
    private Request request;
    private User loginUser;
    private String backPage;

    @FXML
    private Stage currentPopupStage;
    @FXML
    private Stage currentNotApprove;
    @FXML
    private HBox approveButtonHbox;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private HBox subjectHbox;
    @FXML
    private Label titleLabel;
    @FXML
    private BorderPane borderPane;
    @FXML
    public Button backButton;
    @FXML
    public AnchorPane mainAnchorPane;

    private AdvisorPageController advisorPageController;

    public void setAdvisorPageController(AdvisorPageController advisorPageController) {
        this.advisorPageController = advisorPageController;
    }

    public void initializeMainInformation() {

        if (!backButton.isVisible()) {
            updateStyle();
        }
        scrollPane.requestFocus();// ให้ ScrollPane ได้รับโฟกัสแทน
        if (!request.getStatusNext().equals("คำร้องส่งต่อให้อาจารย์ที่ปรึกษา")) {
            approveButtonHbox.setDisable(true);
        }
        if (request instanceof GeneralRequestForm) {
            loadGeneralInformation();
        } else if (request instanceof RegisterRequestForm) {
            loadRegisterInformation();
        } else if (request instanceof AcademicLeaveRequestForm) {
            loadAcademicInformation();
        } else if (request instanceof Ku1AndKu3RequestForm) {
            if (request.getRequestType().equalsIgnoreCase("KU1")) {
                loadKU1Information();
            } else {
                loadKU3Information();
            }
        }
        if (backPage != null && backPage.equalsIgnoreCase("advisorRequest")) {
            approveButtonHbox.setVisible(true);
        }
    }

    public void setTitleLabel(String title) {
        titleLabel.setText(title);
    }

    public void setLoginUser(User loginUser) {
        this.loginUser = loginUser;

    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }
    public void setBackPage(String backPage) {
        this.backPage = backPage;
    }

    @FXML
    private void onBackButtonClick() {
        if (backPage != null && backPage.equalsIgnoreCase("advisorRequest")) {
            advisorPageController.onRequestsClicked();
        } else if (backPage != null && backPage.equalsIgnoreCase("student")) {
            goToStudentPage(backPage);
        } else if (backPage != null && backPage.equalsIgnoreCase("advisorStudentRequest")) {
            goToStudentPage(backPage);
        }

    }

    private void goToAdvisorRequest() {
        try {
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
        }
    }

    private void goToStudentPage(String destination) {
        try {
            String viewPath = "/ku/cs/views/student-request-info-pane.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            StudentRequestInfoController controller = fxmlLoader.getController();
            controller.setLoginUser((Student) loginUser);
            controller.setRequest(request);
            controller.setBackPage(destination);
            controller.initialize();
            controller.showInfo();
            controller.showTable();
            borderPane.setCenter(pane);
            controller.setBorderPane(borderPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadGeneralInformation(){
        try {
            String viewPath = "/ku/cs/views/general-request-form-information.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            VBox pane = fxmlLoader.load();
            GeneralInformaitonRequestFormController controller = fxmlLoader.getController();
            controller.setLoginUser(loginUser);
            controller.setRequest(request);
            controller.setBackPage(backPage);
            controller.setBorderPane(borderPane);
            controller.showData();
            scrollPane.setContent(pane);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void loadRegisterInformation(){
        try {
            String viewPath = "/ku/cs/views/register-request-form-information.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            VBox pane = fxmlLoader.load();
            RegisterInformationRequestFormController controller = fxmlLoader.getController();
            controller.setLoginUser(loginUser);
            controller.setRequest(request);
            controller.setBackPage(backPage);
            controller.setBorderPane(borderPane);
            controller.showData();
            scrollPane.setContent(pane);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void loadAcademicInformation(){
        try {
            String viewPath = "/ku/cs/views/academic-leave-form-information.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            VBox pane = fxmlLoader.load();
            AcademicLeaveInformationRequestFormController controller = fxmlLoader.getController();
            controller.setLoginUser(loginUser);
            controller.setRequest(request);
            controller.setBackPage(backPage);
            controller.setBorderPane(borderPane);
            controller.showData();
            scrollPane.setContent(pane);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void loadKU1Information(){
        try {
            String viewPath = "/ku/cs/views/ku1-form-information.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            VBox pane = fxmlLoader.load();
            Ku1InformationRequestFormController controller = fxmlLoader.getController();
            controller.setLoginUser(loginUser);
            controller.setRequest(request);
            controller.setBackPage(backPage);
            controller.setBorderPane(borderPane);
            controller.showData();
            scrollPane.setContent(pane);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void loadKU3Information(){
        try {
            String viewPath = "/ku/cs/views/ku3-form-information.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            VBox pane = fxmlLoader.load();
            Ku3InformationRequestFormController controller = fxmlLoader.getController();
            controller.setLoginUser(loginUser);
            controller.setRequest(request);
            controller.setBackPage(backPage);
            controller.setBorderPane(borderPane);
            controller.showData();
            scrollPane.setContent(pane);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @FXML
    private void approve() {
        LocalDateTime now = LocalDateTime.now();
        RequestListFileDatasource requestListDatasource = new RequestListFileDatasource("data");
        ApproverListFileDatasource approverListFileDatasource = new ApproverListFileDatasource("data");
        RequestList requestList = requestListDatasource.readData();
        ApproverList approverList = approverListFileDatasource.readData();
        Request targetRequest = requestList.findByRequestUUID(request.getUuid());
        Approver approver = approverList.getApproverList(targetRequest.getUuid()).getApprovers("advisor").stream().toList().getFirst();
        try {
            approver.setStatus("เรียบร้อย");
        } catch (ApproverStatusException e) {
            System.out.println(e.getMessage());
        }
        requestListDatasource.appendToLog(targetRequest);
        targetRequest.setStatusNow("อนุมัติโดยอาจารย์ที่ปรึกษา");
        targetRequest.setStatusNext("คำร้องส่งต่อให้หัวหน้าภาควิชา");
        targetRequest.setTimeStamp(now);
        requestListDatasource.writeData(requestList);
        approverListFileDatasource.writeData(approverList);
        goToAdvisorRequest();
    }

    @FXML
    private void notApprove() {
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
                notApproveController.setAdvisorPageController(advisorPageController);
                notApproveController.setBorderPane(borderPane);
                scene.getStylesheets().add(getClass().getResource("/ku/cs/styles/error-confirm-edit-page-style.css").toExternalForm());
                currentNotApprove.setScene(scene);
                currentNotApprove.initModality(Modality.APPLICATION_MODAL);
                currentNotApprove.setTitle("Not Approve");
                currentNotApprove.show();
            }
    }

    public void setBackPageVisible(boolean status) {
        backButton.setVisible(status);
    }

    public void setCurrentPopupStage(Stage currentPopupStage) {
        this.currentPopupStage = currentPopupStage;
    }

    public void updateStyle() {
        Theme.getInstance().loadCssToPage(mainAnchorPane, new PathGenerator() {
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
}
