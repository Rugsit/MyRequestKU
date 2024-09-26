package ku.cs.controllers.faculty;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ku.cs.models.faculty.Faculty;
import ku.cs.models.request.approver.Approver;
import ku.cs.models.request.approver.ApproverList;
import ku.cs.models.request.approver.DepartmentApprover;
import ku.cs.models.request.approver.FacultyApprover;
import ku.cs.models.user.DepartmentUser;
import ku.cs.models.user.FacultyUser;
import ku.cs.models.user.User;
import ku.cs.services.ApproverListFileDatasource;

public class AddApproverController {

    @FXML
    private TextField nameTextField;
    @FXML
    private TextField lastnameTextField;
    @FXML
    private TextField academicRoleTextField;
    @FXML
    private Label errorLabel;
    private Stage stage;

    private User loginUser;

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
                if (loginUser instanceof FacultyUser) {
                    approver = new FacultyApprover("faculty", ((FacultyUser) loginUser).getFacultyUUID().toString(), academicRole, name, lastName);
                } else if (loginUser instanceof DepartmentUser) {
                    approver = new DepartmentApprover("department", ((DepartmentUser) loginUser).getDepartmentUUID().toString(), academicRole, name, lastName);
                }
                System.out.println("test");
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

    public void setLoginUser(User loginUser) {
        this.loginUser = loginUser;
    }
}
