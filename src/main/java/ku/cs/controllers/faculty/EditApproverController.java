package ku.cs.controllers.faculty;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.w3c.dom.Text;

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

    public  void setApproverDetail(String approverName, String approverLastName, String approverRole){
        this.approverName = approverName;
        this.approverLastName = approverLastName;
        this.approverRole = approverRole;
        nameLabel.setText(approverName);
        lastNameLabel.setText(approverLastName);
        academicRoleLabel.setText(approverRole);
    }

    private void changeNewAdvisorDetail(){

    }

    @FXML
    private void onAcceptClick() {
        System.out.println("Accept button clicked");
    }

    @FXML
    private void onExitClick() {
        System.out.println("Exit button clicked");
    }
}
