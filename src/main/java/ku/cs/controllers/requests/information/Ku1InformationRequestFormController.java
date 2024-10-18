package ku.cs.controllers.requests.information;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import ku.cs.models.request.Ku1AndKu3RequestForm;
import ku.cs.models.request.Request;
import ku.cs.models.user.Advisor;
import ku.cs.models.user.Student;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.services.Datasource;
import ku.cs.services.UserListFileDatasource;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Ku1InformationRequestFormController {
    private Ku1AndKu3RequestForm request;
    private User loginUser;

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
    private VBox prototypeVbox;
    @FXML
    private RadioButton secondRadio;
    @FXML
    private VBox subjectVbox;
    @FXML
    private RadioButton summerRadio;
    @FXML
    private Label surnameTextField;
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
            subjectVbox.getChildren().add(deepCopyVbox(prototypeVbox, eachList));
        }
        subjectVbox.getChildren().removeFirst();
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


    private VBox deepCopyVbox(VBox vbox, ArrayList<String> data) {
        int[] count = {0};
        VBox newVbox = new VBox();
        VBox.setMargin(newVbox, VBox.getMargin(vbox));
        for (Node node : vbox.getChildren()) {
            newVbox.getChildren().add(deepCopyHBox((HBox) node, count, data));
        }
        newVbox.setPrefHeight(vbox.getPrefHeight());
        return newVbox;
    }

    private HBox deepCopyHBox(HBox hbox, int[] count, ArrayList<String> data) {
        ToggleGroup toggleGroup = new ToggleGroup();
        HBox newHbox = new HBox();
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
                    newLabel.setText(data.get(count[0]));
                    count[0]++;
                    newLabel.setWrapText(true);
                }
                newLabel.setPrefWidth(label.getPrefWidth());
                newLabel.setPrefHeight(label.getPrefHeight());
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
