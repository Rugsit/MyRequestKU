package ku.cs.controllers.requests.information;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import ku.cs.controllers.advisor.AdvisorRequestsController;
import ku.cs.controllers.student.StudentRequestInfoController;
import ku.cs.models.request.Ku1AndKu3RequestForm;
import ku.cs.models.request.Request;
import ku.cs.models.user.Advisor;
import ku.cs.models.user.Student;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.services.Datasource;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

public class Ku3InformationRequestFormController {
    private Ku1AndKu3RequestForm request;
    private User loginUser;
    private String backPage;

    @FXML
    private Label advisorIdTextField;
    @FXML
    private Label advisorTextField;
    @FXML
    private TextField campusTextField;
    @FXML
    private Label createDateTextField;
    @FXML
    private Label departmentTextField;
    @FXML
    private Label facultyTextField;
    @FXML
    private RadioButton firstRadio;
    @FXML
    private Label gmailTextField;
    @FXML
    private Label idTextField;
    @FXML
    private RadioButton interRadio;
    @FXML
    private Label nameTextField;
    @FXML
    private CheckBox part1Checkbox;
    @FXML
    private CheckBox part2CheckBox;
    @FXML
    private VBox prototypePart1Vbox;
    @FXML
    private VBox prototypePart2Vbox;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private RadioButton secondRadio;
    @FXML
    private VBox subjectDropVbox;
    @FXML
    private VBox subjectVbox;
    @FXML
    private RadioButton summerRadio;
    @FXML
    private Label surnameTextField;
    @FXML
    private TextField teacherTextField;
    @FXML
    private TextField teacherTextField1;
    @FXML
    private TextField telTextField;
    @FXML
    private RadioButton thaiRadio;
    @FXML
    private Label updateDateTextField;
    @FXML
    private TextField yearTextField;
    @FXML
    private BorderPane borderPane;

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
        if (request.getCurriculum().equals("Thai")) {
            thaiRadio.setSelected(true);
        } else if (request.getCurriculum().equals("International")) {
            interRadio.setSelected(true);
        }
        telTextField.setText(request.getTel());
        if (request.getSemester().equals("First")) {
            firstRadio.setSelected(true);
        } else if (request.getSemester().equals("Second")) {
            secondRadio.setSelected(true);
        } else if (request.getSemester().equals("Summer")) {
            summerRadio.setSelected(true);
        }
        yearTextField.setText(request.getYear());
        campusTextField.setText(request.getCampus());
        for (ArrayList<String> eachList : request.getSubjectList()) {
            if (eachList.getLast().equalsIgnoreCase("add")) {
                subjectVbox.getChildren().add(deepCopyVbox(prototypePart1Vbox, eachList));
            } else if(eachList.getLast().equalsIgnoreCase("drop")) {
                subjectDropVbox.getChildren().add(deepCopyVbox(prototypePart2Vbox, eachList));
            }
        }
        subjectVbox.getChildren().removeFirst();
        subjectDropVbox.getChildren().removeFirst();
        if (subjectVbox.getChildren().isEmpty()) {
            part1Checkbox.setDisable(true);
        } else {
            part1Checkbox.setSelected(true);
        }
        if (subjectDropVbox.getChildren().isEmpty()) {
            part2CheckBox.setDisable(true);
        } else {
            part2CheckBox.setSelected(true);
        }
    }

    public void setLoginUser(User loginUser) {
        this.loginUser = loginUser;
    }

    public void setRequest(Request request) {
        this.request = (Ku1AndKu3RequestForm) request;
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    public void setBackPage(String backPage) {
        this.backPage = backPage;
    }


    private VBox deepCopyVbox(VBox vbox, ArrayList<String> data) {
        int[] count = {0};
        VBox newVbox = new VBox();
        VBox.setMargin(newVbox, VBox.getMargin(vbox));
        for (Node node : vbox.getChildren()) {
            newVbox.getChildren().add(deepCopyHBox((HBox) node, count, data));
        }
        return newVbox;
    }

    private HBox deepCopyHBox(HBox hbox, int[] count, ArrayList<String> data) {
        ToggleGroup toggleGroup = new ToggleGroup();
        HBox newHbox = new HBox();
        newHbox.setAlignment(hbox.getAlignment());
        VBox.setMargin(newHbox, VBox.getMargin(hbox));
        for (Node node : hbox.getChildren()) {
            if (node instanceof Label) {
                Label label = (Label) node;
                Label newLabel = new Label(label.getText());
                HBox.setMargin(newLabel, HBox.getMargin(label));
                newLabel.getStyleClass().addAll(label.getStyleClass());
                newHbox.getChildren().add(newLabel);
            } else if (node instanceof TextField) {
                TextField textField = (TextField) node;
                TextField newTextField = new TextField();
                newTextField.setText(data.get(count[0]));
                newTextField.setPrefHeight(textField.getPrefHeight());
                newTextField.setPrefWidth(textField.getPrefWidth());
                HBox.setMargin(newTextField, HBox.getMargin(textField));
                newTextField.getStyleClass().addAll(textField.getStyleClass());
                newHbox.getChildren().add(newTextField);
                count[0]++;
            } else if (node instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) node;
                RadioButton newRadioButton = new RadioButton(radioButton.getText());
                HBox.setMargin(newRadioButton, HBox.getMargin(radioButton));
                newRadioButton.getStyleClass().addAll(radioButton.getStyleClass());
                newRadioButton.setToggleGroup(toggleGroup);
                if (data.get(count[0]).equalsIgnoreCase(newRadioButton.getText())) {
                    newRadioButton.setSelected(true);
                    count[0]++;
                }
                newHbox.getChildren().add(newRadioButton);

            }
        }
        return newHbox;
    }
}
