package ku.cs.controllers.department;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ku.cs.models.request.Request;
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

    private String approverType = "approver";
    private Request request;

    @FXML
    private Label optionalRoleTextField;

    @FXML
    private ChoiceBox<String> roleChoiceBox;

    private String[] roles = {"หัวหน้าภาควิชา", "รองหัวหน้าภาควิชา", "รักษาการณ์แทนหัวหน้าภาควิชา",
            "คณบดี", "รองคณบดี", "รักษาการณ์แทนคณบดี", "อื่น ๆ"};

    @FXML
    public void initialize(){
        academicRoleTextField.setVisible(false);
        optionalRoleTextField.setVisible(false);
        roleChoiceBox.setItems(FXCollections.observableArrayList(roles));
        roleChoiceBox.setValue(roles[0]);

        roleChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("อื่น ๆ")) {
                optionalRoleTextField.setVisible(true);
                academicRoleTextField.setVisible(true);
            } else {
                optionalRoleTextField.setVisible(false);
                academicRoleTextField.setVisible(false);
            }
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void writeApprover() {
        String name = nameTextField.getText().trim();
        String lastName = lastnameTextField.getText().trim();
        String academicRole = academicRoleTextField.getText().trim();

        if (roleChoiceBox.getValue().equals("อื่น ๆ")) {
            academicRole = academicRoleTextField.getText().trim();
        } else {
            academicRole = roleChoiceBox.getValue();
        }


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
}
