package ku.cs.controllers.department;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ku.cs.models.request.Request;
import ku.cs.models.request.approver.Approver;
import ku.cs.models.request.approver.DepartmentApprover;
import ku.cs.models.request.approver.FacultyApprover;
import ku.cs.models.user.DepartmentUser;
import ku.cs.models.user.FacultyUser;
import ku.cs.models.user.User;
import ku.cs.services.ApproverListFileDatasource;
import ku.cs.services.PathGenerator;
import ku.cs.services.Theme;

public class AddDepartmentApproverController {

    @FXML
    private TextField nameTextField;
    @FXML
    private TextField lastnameTextField;
    @FXML
    private TextField academicRoleTextField;
    @FXML
    private Label errorLabel;
    @FXML
    private AnchorPane anchorPane;
    private Stage stage;

    private DepartmentUser loginUser;

    private String approverType = "approver";
    private Request request;

    @FXML
    private void initialize() {
        Platform.runLater(this::updateStyle);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void writeApprover() {
        String name = nameTextField.getText().trim();
        String lastName = lastnameTextField.getText().trim();
        String academicRole = academicRoleTextField.getText().trim();

        if (!name.isEmpty() && !lastName.isEmpty() && !academicRole.isEmpty()) {
            errorLabel.setVisible(false);
            ApproverListFileDatasource approverDatasource = new ApproverListFileDatasource(approverType);
            try {
                Approver approver = null;
                approver = new DepartmentApprover("department", ((DepartmentUser) loginUser).getDepartmentUUID().toString(), academicRole, name, lastName);
                if (!approverType.equals("approver") && request != null) {
                    approver.setRequestUUID(request);
                }
                approverDatasource.appendData(approver, approverType);
            } catch (Exception e) {
                errorLabel.setVisible(true);
                errorLabel.setText("ไม่สามารถบันทึกข้อมูลลงในฐานข้อมูลได้");
            }
            stage.close();
        } else {
            errorLabel.setVisible(true);
            errorLabel.setText("โปรดกรอกข้อมูลให้ครบถ้วน");
        }
    }

    @FXML
    private void onAcceptClick() {
        writeApprover();
    }

    @FXML
    private void onExitClick() {
        if (stage != null) {
            stage.close();
        }
    }

    public void setLoginUser(DepartmentUser loginUser) {
        this.loginUser = loginUser;
    }

    public void setApproverType(String approverType) {
        if (approverType != null) {
            this.approverType = approverType;
        }
    }

    public void setCurrentRequest(Request request) {
        if (request != null) {
            this.request = request;
        }
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
}
