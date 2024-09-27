package ku.cs.controllers.department;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ku.cs.models.request.approver.Approver;
import ku.cs.models.request.approver.DepartmentApprover;
import ku.cs.models.request.approver.FacultyApprover;
import ku.cs.models.user.DepartmentUser;
import ku.cs.models.user.FacultyUser;
import ku.cs.models.user.User;
import ku.cs.services.ApproverListFileDatasource;

public class AddDepartmentApproverController {

    @FXML
    private TextField nameTextField;
    @FXML
    private TextField lastnameTextField;
    @FXML
    private TextField academicRoleTextField;
    @FXML
    private Label errorLabel;
    private Stage stage;

    private DepartmentUser loginUser;


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void writeApprover() {
        String name = nameTextField.getText().trim();
        String lastName = lastnameTextField.getText().trim();
        String academicRole = academicRoleTextField.getText().trim();

        if (!name.isEmpty() && !lastName.isEmpty() && !academicRole.isEmpty()) {
            errorLabel.setVisible(false);
            ApproverListFileDatasource approverDatasource = new ApproverListFileDatasource("approver");
            try {
                Approver approver = null;
                approver = new DepartmentApprover("department", ((DepartmentUser) loginUser).getDepartmentUUID().toString(), academicRole, name, lastName);
                approverDatasource.appendData(approver, "approver");
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
}
