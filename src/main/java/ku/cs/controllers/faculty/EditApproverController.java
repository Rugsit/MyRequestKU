package ku.cs.controllers.faculty;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ku.cs.models.request.approver.Approver;
import ku.cs.models.request.approver.ApproverList;
import ku.cs.models.request.approver.FacultyApprover;
import ku.cs.services.ApproverListFileDatasource;
import org.w3c.dom.Text;

import java.util.List;
import java.util.stream.Collectors;

public class EditApproverController {
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField academicRoleTextField;
    @FXML
    private Label nameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label academicRoleLabel;
    private String approverName;
    private String approverLastName;
    private String approverRole;
    private String newApproverName;
    private String newApproverLastName;
    private String newApproverRole;
    private Approver approver;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setApprover(Approver approver){
        this.approver = approver;
    }

    public  void setApproverDetail(String approverName, String approverLastName, String approverRole){
        nameLabel.setText(approverName);
        lastNameLabel.setText(approverLastName);
        academicRoleLabel.setText(approverRole);
    }

    private void changeNewAdvisorDetail() {
        String newApproverName = nameTextField.getText().trim();
        String newApproverLastName = lastNameTextField.getText().trim();
        String newApproverRole = academicRoleTextField.getText().trim();
        ApproverListFileDatasource approverDatasource = new ApproverListFileDatasource("approver");

        ApproverList approverList = approverDatasource.readData();
        approver = approverList.findApproverByObject(approver);

        approver.setFirstname(newApproverName);
        approver.setLastname(newApproverLastName);
        approver.setRole(newApproverRole);

        approverDatasource.writeData(approverList);
        stage.close();
    }

    @FXML
    private void onAcceptClick() {
        System.out.println("Accept button clicked");
        changeNewAdvisorDetail();
    }

    @FXML
    private void onExitClick() {
        System.out.println("Exit button clicked");
        if (stage != null) {
            stage.close();
        }
    }
}
