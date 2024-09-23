package ku.cs.controllers.faculty;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ku.cs.models.faculty.Faculty;
import ku.cs.models.request.approver.ApproverList;
import ku.cs.models.request.approver.FacultyApprover;
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

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void writeApprover() {
        // Retrieve the input values here
        String name = nameTextField.getText().trim();
        String lastName = lastnameTextField.getText().trim();
        String academicRole = academicRoleTextField.getText().trim();

        if (!name.isEmpty() && !lastName.isEmpty() && !academicRole.isEmpty()) {
            errorLabel.setVisible(false);
            ApproverListFileDatasource approverDatasource = new ApproverListFileDatasource("approver");
            try {
                FacultyApprover FApprover = new FacultyApprover("faculty", academicRole, name, lastName);
                approverDatasource.appendData(FApprover, "approver");
            } catch (Exception e) {
                errorLabel.setVisible(true);
                errorLabel.setText("ไม่สามารถบันทึกข้อมูลลงในฐานข้อมูลได้");
            }
            stage.close(); // Close the window after successful save
        } else {
            errorLabel.setVisible(true);
            errorLabel.setText("โปรดกรอกข้อมูลให้ครบถ้วน");
        }
    }

    @FXML
    private void onAcceptClick() {
        System.out.println("Accept button clicked");
        writeApprover(); // Call writeApprover() to handle the logic
    }

    @FXML
    private void onExitClick() {
        System.out.println("Exit button clicked");
        if (stage != null) {
            stage.close(); // Close the window when exit button is clicked
        }
    }
}
