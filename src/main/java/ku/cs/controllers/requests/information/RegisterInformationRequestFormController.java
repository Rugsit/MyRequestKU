package ku.cs.controllers.requests.information;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ku.cs.controllers.advisor.AdvisorRequestsController;
import ku.cs.controllers.student.StudentRequestInfoController;
import ku.cs.models.request.GeneralRequestForm;
import ku.cs.models.request.RegisterRequestForm;
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

public class RegisterInformationRequestFormController {
    private RegisterRequestForm request;
    private User loginUser;
    private String backPage;

    @FXML
    private HBox approveButtonHbox;
    @FXML
    private BorderPane borderPane;
    @FXML
    private RadioButton addDropRadio;
    @FXML
    private Label advisorIdTextField;
    @FXML
    private Label advisorTextField;
    @FXML
    private VBox buttonCOntainerVbox;
    @FXML
    private Label createDateTextField;
    @FXML
    private Label departmentTextField;
    @FXML
    private Label facultyTextField;
    @FXML
    private RadioButton firstSemesRadio;
    @FXML
    private Label gmailTextField;
    @FXML
    private Label idTextField;
    @FXML
    private RadioButton latePayFirstSemesRadio;
    @FXML
    private RadioButton latePaySecondSemesRadio;
    @FXML
    private RadioButton latePaySummerSemesRadio;
    @FXML
    private TextField latePayYearTextField;
    @FXML
    private RadioButton latePaymentRadio;
    @FXML
    private RadioButton lateRegistrationRadio;
    @FXML
    private Label nameTextField;
    @FXML
    private TextField newCredit;
    @FXML
    private Label newFalTextField;
    @FXML
    private TextField oldCredit;
    @FXML
    private Label oldFalTextField;
    @FXML
    private Label otherTextArea;
    @FXML
    private RadioButton registerBelowNineRadio;
    @FXML
    private RadioButton registerRadio;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private RadioButton secondSemesRadio;
    @FXML
    private RadioButton summerSemesRadio;
    @FXML
    private Label surnameTextField;
    @FXML
    private RadioButton transferFalRadio;
    @FXML
    private Label updateDateTextField;
    @FXML
    private TextField yearTextField;
    @FXML
    private VBox above22Vbox;
    @FXML
    private VBox latePaymentVbox;
    @FXML
    private VBox transferFacultyVbox;

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
        if (request.isLateRegister()) {
            lateRegistrationRadio.setSelected(true);
        } else {
            lateRegistrationRadio.setDisable(true);
        }

        if (request.isAddDrop()) {
            addDropRadio.setSelected(true);
        } else {
            addDropRadio.setDisable(true);
        }

        if (request.isRegisterMoreThan22()) {
            registerRadio.setSelected(true);
            if (request.getSemester().equals("First")) firstSemesRadio.setSelected(true);
            else if (request.getSemester().equals("Second")) secondSemesRadio.setSelected(true);
            else if (request.getSemester().equals("Summer")) summerSemesRadio.setSelected(true);
            yearTextField.setText(String.valueOf(request.getSemesterYear()));
            oldCredit.setText(String.valueOf(request.getOldCredit()));
            newCredit.setText(String.valueOf(request.getNewCredit()));
        } else {
            above22Vbox.setDisable(true);
        }

        if (request.isRegisterLessThan9()) {
            registerBelowNineRadio.setSelected(true);
        } else {
            registerBelowNineRadio.setDisable(true);
        }

        if (request.isLatePayment()) {
            latePaymentRadio.setSelected(true);
            if (request.getLatePaymentSemester().equals("First")) latePayFirstSemesRadio.setSelected(true);
            else if (request.getLatePaymentSemester().equals("Second")) latePaySecondSemesRadio.setSelected(true);
            else if (request.getLatePaymentSemester().equals("Summer")) latePaySummerSemesRadio.setSelected(true);
            latePayYearTextField.setText(String.valueOf(request.getLatePaymentYear()));
        } else {
            latePaymentVbox.setDisable(true);
        }

        if (request.isTransferFaculty()) {
            transferFalRadio.setSelected(true);
            oldFalTextField.setText(request.getOldFaculty());
            newFalTextField.setText(request.getNewFaculty());
        } else {
            transferFacultyVbox.setDisable(true);
        }
        otherTextArea.setText(request.getSince());
    }

    public void setLoginUser(User loginUser) {
        this.loginUser = loginUser;
    }

    public void setRequest(Request request) {
        this.request = (RegisterRequestForm) request;
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    public void setBackPage(String backPage) {
        this.backPage = backPage;
    }

    public void setVisibleApproveButton() {
        approveButtonHbox.setVisible(true);
    }
}
