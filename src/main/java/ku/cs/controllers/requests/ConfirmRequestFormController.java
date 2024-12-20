package ku.cs.controllers.requests;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ku.cs.controllers.student.StudentRequestsController;
import ku.cs.models.request.*;
import ku.cs.models.request.approver.ApproverList;
import ku.cs.models.request.approver.exception.ApproverException;
import ku.cs.models.user.Advisor;
import ku.cs.models.user.Student;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.services.*;

import java.io.IOException;
import java.util.ArrayList;

public class ConfirmRequestFormController {
    private Datasource<RequestList> datasource;
    private Stage stage;
    private User loginUser;

    private Request request;
    private Request requestPair;

    @FXML
    public BorderPane borderPane;
    @FXML
    public AnchorPane mainAnchorPane;

    @FXML
    private void initialize() {
        datasource = new RequestListFileDatasource("data");
        updateStyle();
    }

    @FXML
    public void onEditClick() {
        stage.close();
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    public void setRequestForm(Request request) {
        this.request = request;
    }

    public void setRequestPair(Request request) {
        this.requestPair = request;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setLoginUser(User loginUser) { this.loginUser = loginUser;}

    @FXML
    public void onConfirmClick() {
        RequestList requestList = datasource.readData();
        try {
            String viewPath = "/ku/cs/views/student-requests-pane.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            StudentRequestsController controller = fxmlLoader.getController();
            controller.setBorderPane(this.borderPane);
            controller.setLoginUser((Student) loginUser);

            Datasource<ApproverList> approverDatasource = new ApproverListFileDatasource("request");
            Datasource<UserList> userListDatasource = new UserListFileDatasource("data", "advisor.csv");
            UserList userList = userListDatasource.readData();
            ApproverList approverList = approverDatasource.readData();
            Advisor advisor = (Advisor) userList.findUserByUUID(((Student)loginUser).getAdvisor());

            if (this.request != null) {
                request.setDepartmentUUID(((Student) loginUser).getDepartmentUUID());
                request.setFacultyUUID(null);
                request.addApprover(request.getUuid().toString(), "advisor", ((Student) loginUser).getDepartmentUUID().toString(), "อาจารย์ที่ปรึกษา", advisor.getFirstname(), advisor.getLastname());
                approverList.addApprover(request.getUuid().toString(), "advisor", ((Student) loginUser).getDepartmentUUID().toString(), "อาจารย์ที่ปรึกษา", advisor.getFirstname(), advisor.getLastname());
                request.addApprover(request.getUuid().toString(), "department", ((Student) loginUser).getDepartmentUUID().toString(), "หัวหน้าภาควิชา", "default", "default");
                approverList.addApprover(request.getUuid().toString(), "department", ((Student) loginUser).getDepartmentUUID().toString(), "หัวหน้าภาควิชา", "default", "default");
                request.addApprover(request.getUuid().toString(), "faculty",((Student) loginUser).getFacultyUUID().toString(), "คณบดี", "default", "default");
                approverList.addApprover(request.getUuid().toString(), "faculty", ((Student) loginUser).getFacultyUUID().toString(), "คณบดี", "default", "default");
                if (request instanceof AcademicLeaveRequestForm) {
                    ArrayList<String> subject = ((AcademicLeaveRequestForm) request).getSubject();
                    for (int i = 1; i < subject.size(); i+= 2) {
                        String[] arr = subject.get(i).split(" +");
                        String firstname = arr[0].trim();
                        String lastname;
                        if (arr.length > 1) { lastname = arr[1].trim();}
                        else lastname = "นามสกุล";
                        request.addApprover(request.getUuid().toString(), "other",((Student) loginUser).getDepartmentUUID().toString(), "อาจารย์ประจำวิชา", firstname, lastname);
                        approverList.addApprover(request.getUuid().toString(), "other",((Student) loginUser).getDepartmentUUID().toString(), "อาจารย์ประจำวิชา", firstname, lastname);
                    }
                }
                requestList.addRequest(request);
            }
            if (this.requestPair != null) {
                requestPair.setDepartmentUUID(((Student) loginUser).getDepartmentUUID());
                requestPair.setFacultyUUID(null);
                requestPair.addApprover(requestPair.getUuid().toString(), "advisor",((Student) loginUser).getDepartmentUUID().toString(), "อาจารย์ที่ปรึกษา", advisor.getFirstname(), advisor.getLastname());
                approverList.addApprover(requestPair.getUuid().toString(), "advisor",((Student) loginUser).getDepartmentUUID().toString(), "อาจารย์ที่ปรึกษา", advisor.getFirstname(), advisor.getLastname());
                requestPair.addApprover(requestPair.getUuid().toString(), "department", ((Student) loginUser).getDepartmentUUID().toString(), "หัวหน้าภาควิชา", "default", "default");
                approverList.addApprover(requestPair.getUuid().toString(), "department", ((Student) loginUser).getDepartmentUUID().toString(), "หัวหน้าภาควิชา", "default", "default");
                requestPair.addApprover(requestPair.getUuid().toString(), "faculty",((Student) loginUser).getFacultyUUID().toString(), "คณบดี", "default", "default");
                approverList.addApprover(requestPair.getUuid().toString(), "faculty", ((Student) loginUser).getFacultyUUID().toString(), "คณบดี", "default", "default");
                for (ArrayList<String> each : ((Ku1AndKu3RequestForm)requestPair).getSubjectList()) {
                    String[] arr = each.get(6).split("[ \t]+");
                    String firstname = arr[0].trim();
                    String lastname = arr[1].trim();
                    requestPair.addApprover(requestPair.getUuid().toString(), "other", ((Student) loginUser).getDepartmentUUID().toString(), "อาจารย์ประจำวิชา", firstname, lastname);
                    approverList.addApprover(requestPair.getUuid().toString(), "other",((Student) loginUser).getDepartmentUUID().toString(), "อาจารย์ประจำวิชา", firstname, lastname);
                }
                requestList.addRequest(requestPair);
            }
            approverDatasource.writeData(approverList);
            datasource.writeData(requestList);
            controller.showTable();
            borderPane.setCenter(pane);
            stage.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ApproverException e) {
            throw new RuntimeException(e);
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
}
