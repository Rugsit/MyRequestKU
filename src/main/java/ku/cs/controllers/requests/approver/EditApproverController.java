package ku.cs.controllers.requests.approver;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ku.cs.models.request.approver.Approver;
import ku.cs.models.request.approver.ApproverList;
import ku.cs.models.request.approver.DepartmentApprover;
import ku.cs.models.request.approver.FacultyApprover;
import ku.cs.services.ApproverListFileDatasource;
import ku.cs.services.PathGenerator;
import ku.cs.services.Theme;

import java.util.ArrayList;
import java.util.Arrays;

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
    @FXML
    private AnchorPane anchorPane;
    private String approverName;
    private String approverLastName;
    private String approverRole;
    private String newApproverName;
    private String newApproverLastName;
    private String newApproverRole;
    private Approver approver;
    private Stage stage;

    @FXML
    private ChoiceBox<String> roleChoiceBox;

    private ArrayList<String> roles = new ArrayList<>(Arrays.asList("หัวหน้าภาควิชา", "รองหัวหน้าภาควิชา", "รักษาการณ์แทนหัวหน้าภาควิชา",
            "คณบดี", "รองคณบดี", "รักษาการณ์แทนคณบดี", "อื่น ๆ"));

    @FXML
    private Label optionalRoleLabel;

    @FXML
    public void initialize(){
        academicRoleTextField.setVisible(false);
        optionalRoleLabel.setVisible(false);
        updateStyle();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setApprover(Approver approver){
        if (approver == null){ return; }
        this.approver = approver;

        if (approver instanceof DepartmentApprover){
            roles = new ArrayList<>(Arrays.asList("หัวหน้าภาควิชา", "รองหัวหน้าภาควิชา", "รักษาการณ์แทนหัวหน้าภาควิชา", "อื่น ๆ"));
        } else if (approver instanceof FacultyApprover){
            roles = new ArrayList<>(Arrays.asList("คณบดี", "รองคณบดี", "รักษาการณ์แทนคณบดี", "อื่น ๆ"));
        }

        roleChoiceBox.setItems(FXCollections.observableArrayList(roles));
        roleChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("อื่น ๆ")) {
                optionalRoleLabel.setVisible(true);
                academicRoleTextField.setVisible(true);
                academicRoleTextField.setText(approver.getRole());
            } else {
                optionalRoleLabel.setVisible(false);
                academicRoleTextField.setVisible(false);
            }
        });
        if (roles.contains(approver.getRole())) {
            roleChoiceBox.setValue(roles.get(roles.indexOf(approver.getRole())));
        } else {
            roleChoiceBox.setValue(roles.get(roles.indexOf("อื่น ๆ")));
        }

    }

    public  void setApproverDetail(String approverName, String approverLastName, String approverRole){
        nameLabel.setText(approverName);
        lastNameLabel.setText(approverLastName);
        academicRoleLabel.setText(approverRole);
        nameTextField.setText(approverName);
        lastNameTextField.setText(approverLastName);
        academicRoleTextField.setText(approverRole);
    }

    private void changeNewApproverDetail() {
        String newApproverName = nameTextField.getText().trim();
        String newApproverLastName = lastNameTextField.getText().trim();
        String newApproverRole = academicRoleTextField.getText().trim();

        if (roleChoiceBox.getValue().equals("อื่น ๆ")) {
            newApproverRole = academicRoleTextField.getText().trim();
        } else {
            newApproverRole = roleChoiceBox.getValue();
        }

        ApproverListFileDatasource approverDatasource;
        if (approver.getRequestUUID() != null) {
            approverDatasource = new ApproverListFileDatasource("request");
        } else {
            approverDatasource = new ApproverListFileDatasource("approver");
        }
        ApproverList approverList = approverDatasource.readData();
        approver = approverList.findApproverByObject(approver);
        if (approver == null) {
            return;
        }

        approver.setFirstname(newApproverName);
        approver.setLastname(newApproverLastName);
        approver.setRole(newApproverRole);

        approverDatasource.writeData(approverList);
        stage.close();
    }

    private void deleteApproverDetail(){
        ApproverListFileDatasource approverDatasource;
        if (approver.getRequestUUID() != null) {
            approverDatasource = new ApproverListFileDatasource("request");
        } else {
            approverDatasource = new ApproverListFileDatasource("approver");
        }
        ApproverList approverList = approverDatasource.readData();
        approverList.deleteApproverByObject(approver);
        approverDatasource.writeData(approverList);
        stage.close();
    }

    @FXML
    private void onAcceptClick() {
        changeNewApproverDetail();
    }

    @FXML
    private void onDeleteClick(){
        deleteApproverDetail();
    }

    @FXML
    private void onExitClick() {
        if (stage != null) {
            stage.close();
        }
    }

    public void updateStyle() {
        Theme.getInstance().loadCssToPage(anchorPane, new PathGenerator() {
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
