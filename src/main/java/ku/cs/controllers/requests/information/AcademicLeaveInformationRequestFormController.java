package ku.cs.controllers.requests.information;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import ku.cs.models.request.AcademicLeaveRequestForm;
import ku.cs.models.request.Request;
import ku.cs.models.user.Advisor;
import ku.cs.models.user.Student;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.services.Datasource;
import ku.cs.services.UserListFileDatasource;
import java.time.format.DateTimeFormatter;


public class AcademicLeaveInformationRequestFormController {
    private AcademicLeaveRequestForm request;
    private User loginUser;

    @FXML
    private Label subjectHaveRegister;
    @FXML
    private VBox subjectVbox;
    @FXML
    private HBox subjectHbox;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Label addressTextArea;
    @FXML
    private Label advisorIdTextField;
    @FXML
    private Label advisorTextField;
    @FXML
    private TextField amountLeaveTextField;
    @FXML
    private Label createDateTextField;
    @FXML
    private Label departmentTextField;
    @FXML
    private Label facultyTextField;
    @FXML
    private RadioButton fromFirstSemesterRadio;
    @FXML
    private RadioButton fromSecondSemesterRadio;
    @FXML
    private TextField fromYearTextField;
    @FXML
    private Label gmailTextField;
    @FXML
    private Label idTextField;
    @FXML
    private Label nameTextField;
    @FXML
    private RadioButton registerFirstSemester;
    @FXML
    private RadioButton registerRadio;
    @FXML
    private RadioButton registerSecondSemester;
    @FXML
    private TextField registerYearTextField;
    @FXML
    private Label sinceLeaveTextArea;
    @FXML
    private Label surnameTextField;
    @FXML
    private TextField telTextField;
    @FXML
    private RadioButton toFirstSemesterRadio;
    @FXML
    private RadioButton toSecondSemesterRadio;
    @FXML
    private TextField toYearTextField;
    @FXML
    private Label updateDateTextField;
    @FXML
    private FlowPane haveRegisterHBox;

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
        addressTextArea.setText(request.getAddress());
        sinceLeaveTextArea.setText(request.getReason());
        amountLeaveTextField.setText(String.valueOf(request.getAmountSemester()));
        if (request.getSemesterFrom().equals("First")) {
            fromFirstSemesterRadio.setSelected(true);
        } else {
            fromSecondSemesterRadio.setSelected(true);
        }
        fromYearTextField.setText(request.getYearFrom());
        if (request.getSemesterTo().equals("First")) {
            toFirstSemesterRadio.setSelected(true);
        } else {
            toSecondSemesterRadio.setSelected(true);
        }
        toYearTextField.setText(request.getYearTo());
        if (request.isHaveRegister()) {
            registerRadio.setSelected(true);
            if (request.getHaveRegisterSemester().equals("First")) {
                registerFirstSemester.setSelected(true);
            } else {
                registerSecondSemester.setSelected(true);
            }
            registerYearTextField.setText(request.getHaveRegisterYear());
            String subjectId = "";
            String subjectAdvisor = "";
            for (int i = 0; i < request.getSubject().size(); i++) {
                if (i % 2 == 0) subjectId = request.getSubject().get(i);
                if (i % 2 != 0) subjectAdvisor = request.getSubject().get(i);
                if (i % 2 != 0) {
                    HBox newHbox = deepCopyHBox(subjectHbox, subjectId, subjectAdvisor);
                    VBox.setMargin(newHbox, VBox.getMargin(subjectHbox));
                    subjectVbox.getChildren().add(newHbox);
                }
            }
            subjectVbox.getChildren().removeFirst();
        } else {
            haveRegisterHBox.setDisable(true);
            subjectHbox.setDisable(true);
            subjectHaveRegister.setVisible(false);
        }
    }

    public void setLoginUser(User loginUser) {
        this.loginUser = loginUser;
    }

    public void setRequest(Request request) {
       this.request = (AcademicLeaveRequestForm) request;
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    private HBox deepCopyHBox(HBox hbox, String subjectId, String subjectAdvisor) {
        int countTextField = 0;
        HBox newHbox = new HBox();
        newHbox.getStyleClass().add(hbox.getStyleClass().getFirst());
        newHbox.setAlignment(hbox.getAlignment());
        VBox.setMargin(newHbox, VBox.getMargin(hbox));
        newHbox.setPrefHeight(hbox.getPrefHeight());
        for (Node node : hbox.getChildren()) {
            if (node instanceof Label) {
                Label label = (Label) node;
                Label newLabel = new Label(label.getText());
                HBox.setMargin(newLabel, HBox.getMargin(label));
                newLabel.getStyleClass().addAll(label.getStyleClass());
                if (label.getText().isEmpty()) {
                    countTextField++;
                    if (countTextField == 1) newLabel.setText(subjectId);
                    if (countTextField == 2) newLabel.setText(subjectAdvisor);
                    newLabel.setPrefWidth(label.getPrefWidth());
                    newLabel.setWrapText(true);
                }
                newHbox.getChildren().add(newLabel);
            } else if (node instanceof TextField) {
                countTextField++;
                TextField textField = (TextField) node;
                TextField newTextField = new TextField();
                if (countTextField == 1) {newTextField.setText(subjectId);}
                if (countTextField == 2) {newTextField.setText(subjectAdvisor);}
                newTextField.setPrefHeight(textField.getPrefHeight());
                newTextField.setPrefWidth(textField.getPrefWidth());
                HBox.setMargin(newTextField, HBox.getMargin(textField));
                newTextField.getStyleClass().addAll(textField.getStyleClass());
                newHbox.getChildren().add(newTextField);
            }
        }
        return newHbox;
    }
}
