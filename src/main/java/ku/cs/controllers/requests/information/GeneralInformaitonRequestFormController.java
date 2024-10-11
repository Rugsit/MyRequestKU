package ku.cs.controllers.requests.information;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ku.cs.controllers.advisor.AdvisorRequestsController;
import ku.cs.controllers.student.StudentRequestInfoController;
import ku.cs.models.request.AcademicLeaveRequestForm;
import ku.cs.models.request.GeneralRequestForm;
import ku.cs.models.request.Request;
import ku.cs.models.user.Advisor;
import ku.cs.models.user.Student;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.services.Datasource;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class GeneralInformaitonRequestFormController {
    private GeneralRequestForm request;
    private User loginUser;
    private String backPage;

    @FXML
    private Label advisorIdTextField;

    @FXML
    private Label advisorTextField;
    @FXML
    private CheckBox changeNameCheckBox;
    @FXML
    private Label createDateTextField;
    @FXML
    private RadioButton damagedRadio;
    @FXML
    private CheckBox degreeCerCheckBox;
    @FXML
    private Label departmentTextField;
    @FXML
    private Label facultyTextField;
    @FXML
    private Label gmailTextField;
    @FXML
    private ToggleGroup group1;
    @FXML
    private Label idTextField;
    @FXML
    private RadioButton lostRadio;
    @FXML
    private Label nameTextField;
    @FXML
    private Label newEngNameTextFeild;
    @FXML
    private Label newEngSurNameTextFeild;
    @FXML
    private Label newThaiNameTextFeild;
    @FXML
    private Label newThaiSurNameTextFeild;
    @FXML
    private Label oldEngNameTextFeild;
    @FXML
    private Label oldEngSurNameTextFeild;
    @FXML
    private Label oldThaiNameTextFeild;
    @FXML
    private Label oldThaiSurNameTextFeild;
    @FXML
    private CheckBox otherCheckBox;
    @FXML
    private Label otherTextArea;
    @FXML
    private CheckBox surNameCheckBox;
    @FXML
    private Label surnameTextField;
    @FXML
    private TextField telTextField;
    @FXML
    private Label updateDateTextField;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private BorderPane borderPane;
    @FXML
    private VBox lostorDamageVbox;
    @FXML
    private VBox changeNameVbox;
    @FXML
    private VBox changeSurNameVbox;
    @FXML
    private VBox otherVbox;

    public void showData() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        createDateTextField.setText(request.getDate().format(formatter));
        updateDateTextField.setText(request.getTimeStamp().format(formatter));
        nameTextField.setText(loginUser.getFirstname());
        surnameTextField.setText(loginUser.getLastname());
        idTextField.setText(String.valueOf(loginUser.getId()));
        facultyTextField.setText(((Student)loginUser).getFaculty());
        departmentTextField.setText(((Student)loginUser).getDepartment());
        gmailTextField.setText(loginUser.getEmail());
        Datasource<UserList> datasource = new UserListFileDatasource("data", "advisor.csv");
        UserList list = datasource.readData();
        Advisor advisor = (Advisor) list.findUserByUUID(((Student)loginUser).getAdvisor());

        if (advisor == null) throw new IllegalArgumentException("ขออภัยไม่พบอาจารย์ที่ปรึกษาที่มีรหัสตรงกับรหัสของอาจารย์ที่ปรึกษาของนักเรียน");
        advisorTextField.setText(advisor.getName());
        advisorIdTextField.setText(advisor.getId());
        telTextField.setText(request.getTel());
        if (request.isDegreeCertificateDamage()) {
            degreeCerCheckBox.setSelected(true);
            damagedRadio.setSelected(true);
        } else if (request.isDegreeCertificateLost()) {
            degreeCerCheckBox.setSelected(true);
            lostRadio.setSelected(true);
        } else {
            lostorDamageVbox.setDisable(true);
        }

        if (request.getOldThaiName().equals("null")) {
            changeNameVbox.setDisable(true);
        } else {
            changeNameCheckBox.setSelected(true);
            oldThaiNameTextFeild.setText(request.getOldThaiName());
            newThaiNameTextFeild.setText(request.getNewThaiName());
            oldEngNameTextFeild.setText(request.getOldEngName());
            newEngNameTextFeild.setText(request.getNewEngName());
        }

        if (request.getOldThaiSurName().equals("null")) {
            changeSurNameVbox.setDisable(true);
        } else {
            surNameCheckBox.setSelected(true);
            oldThaiSurNameTextFeild.setText(request.getOldThaiSurName());
            newThaiSurNameTextFeild.setText(request.getNewThaiSurName());
            oldEngSurNameTextFeild.setText(request.getOldEngSurName());
            newEngSurNameTextFeild.setText(request.getNewEngSurName());
        }

        if (request.getOthers().equals("null")) {
            otherVbox.setDisable(true);
        } else {
            otherCheckBox.setSelected(true);
            otherTextArea.setText(request.getOthers());
        }

    }

    public void setLoginUser(User loginUser) {
        this.loginUser = loginUser;
    }

    public void setRequest(Request request) {
        this.request = (GeneralRequestForm) request;
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }
    public void setBackPage(String backPage) {
        this.backPage = backPage;
    }
}
