package ku.cs.controllers.requests.approver;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ku.cs.models.request.Request;
import ku.cs.models.request.approver.Approver;
import ku.cs.models.user.DepartmentUser;
import ku.cs.models.user.FacultyUser;
import ku.cs.models.user.User;
import ku.cs.services.ApproverListFileDatasource;
import ku.cs.services.PathGenerator;
import ku.cs.services.Theme;

public class AddApproverController{

    @FXML
    private TextField nameTextField;
    @FXML
    private TextField lastnameTextField;
    @FXML
    private TextField academicRoleTextField;
    @FXML
    private Label optionalRoleLabel;
    @FXML
    private Label errorLabel;
    @FXML
    private AnchorPane anchorPane;
    private Stage stage;

    @FXML
    private ChoiceBox<String> roleChoiceBox;

    @FXML Label titleLabel;

    private String[] roles = {"หัวหน้าภาควิชา", "รองหัวหน้าภาควิชา", "รักษาการณ์แทนหัวหน้าภาควิชา",
            "คณบดี", "รองคณบดี", "รักษาการณ์แทนคณบดี", "อื่น ๆ"};

    private User loginUser;

    private String approverType = "approver";
    private Request request;

    @FXML
    public void initialize(){
        academicRoleTextField.setVisible(false);
        optionalRoleLabel.setVisible(false);
        updateStyle();
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
                if (loginUser instanceof DepartmentUser) {
                    approver = new Approver("department", ((DepartmentUser) loginUser).getDepartmentUUID().toString(), academicRole, name, lastName);
                    if (!approverType.equals("approver") && request != null) {
                        approver.setRequestUUID(request);
                    }
                } else if (loginUser instanceof FacultyUser) {
                    approver = new Approver("faculty", ((FacultyUser) loginUser).getFacultyUUID().toString(), academicRole, name, lastName);
                    if (!approverType.equals("approver") && request != null) {
                        approver.setRequestUUID(request);
                        approver.setStatus("รอคณะดำเนินการ");
                    }
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

    public void setLoginUser(User loginUser) {
        this.loginUser = loginUser;
        if (loginUser != null){
            if (loginUser instanceof DepartmentUser){
                roles = new String[]{"หัวหน้าภาควิชา", "รองหัวหน้าภาควิชา", "รักษาการณ์แทนหัวหน้าภาควิชา", "อื่น ๆ"};
                titleLabel.setText("เพิ่มรายชื่อผู้อนุมัติ (ภาควิชา)");
            } else if (loginUser instanceof FacultyUser){
                roles = new String[]{ "คณบดี", "รองคณบดี", "รักษาการณ์แทนคณบดี", "อื่น ๆ"};
                titleLabel.setText("เพิ่มรายชื่อผู้อนุมัติ (คณะ)");
            }
        }
        roleChoiceBox.setItems(FXCollections.observableArrayList(roles));
        roleChoiceBox.setValue(roles[0]);
        roleChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("อื่น ๆ")) {
                optionalRoleLabel.setVisible(true);
                academicRoleTextField.setVisible(true);
            } else {
                optionalRoleLabel.setVisible(false);
                academicRoleTextField.setVisible(false);
            }
        });
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
                return getClass().getResource("/ku/cs/styles/general-dark.css").toString();
            }
            @Override
            public String getThemeLightPath() {
                return getClass().getResource("/ku/cs/styles/general.css").toString();
            }
        });
    }
}
