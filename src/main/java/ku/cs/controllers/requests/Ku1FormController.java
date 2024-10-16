package ku.cs.controllers.requests;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ku.cs.models.request.Ku1AndKu3RequestForm;
import ku.cs.models.request.RegisterRequestForm;
import ku.cs.models.user.User;
import ku.cs.services.SetTransition;
import ku.cs.services.ShowPopupRequest;

import java.io.IOException;
import java.util.ArrayList;

public class Ku1FormController {
    private User loginUser;
    private RegisterRequestForm registerForm;
    private int amountSubject;

    // FXML Component
    @FXML
    private Stage currentConfirmStage;
    @FXML
    private TextField telTextField;
    @FXML
    private RadioButton firstRadio;
    @FXML
    private RadioButton secondRadio;
    @FXML
    private RadioButton summerRadio;
    @FXML
    private RadioButton thaiRadio;
    @FXML
    private RadioButton interRadio;
    @FXML
    private TextField yearTextField;
    @FXML
    private TextField campusTextField;
    @FXML
    private Stage currentErrorStage;
    @FXML
    private BorderPane borderPane;
    @FXML
    private VBox subjectVbox;
    @FXML
    private VBox prototypeVbox;
    @FXML
    private TextField teacherTextField;


    @FXML
    private Button removeButton;
    @FXML
    private Button addButton;

    @FXML
    private Button createFormButton;
    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        SetTransition.setButtonBounce(removeButton);
        SetTransition.setButtonBounce(addButton);
        SetTransition.setButtonBounce(createFormButton);
        SetTransition.setButtonBounce(backButton);
        amountSubject = 1;
    }

    @FXML
    public void addSubject() {
        subjectVbox.getChildren().add(deepCopyVbox(prototypeVbox));
        amountSubject++;
    }

    public void setRegisterForm(RegisterRequestForm registerForm) {
        this.registerForm = registerForm;
    }

    private VBox deepCopyVbox(VBox vbox) {
        VBox newVbox = new VBox();
        VBox.setMargin(newVbox, VBox.getMargin(vbox));
        newVbox.getStyleClass().add(vbox.getStyleClass().getFirst() + amountSubject);
        for (Node node : vbox.getChildren()) {
            newVbox.getChildren().add(deepCopyHBox((HBox) node));
        }
        return newVbox;
    }



    private HBox deepCopyHBox(HBox hbox) {
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
                newTextField.setPrefHeight(textField.getPrefHeight());
                newTextField.setPrefWidth(textField.getPrefWidth());
                HBox.setMargin(newTextField, HBox.getMargin(textField));
                newTextField.getStyleClass().addAll(textField.getStyleClass());
                newHbox.getChildren().add(newTextField);
            } else if (node instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) node;
                RadioButton newRadioButton = new RadioButton(radioButton.getText());
                HBox.setMargin(newRadioButton, HBox.getMargin(radioButton));
                newRadioButton.getStyleClass().addAll(radioButton.getStyleClass());
                newRadioButton.setToggleGroup(toggleGroup);
                newHbox.getChildren().add(newRadioButton);
            }
        }
        return newHbox;
    }

    @FXML
    public void onCreateRegisterForm() {
        Ku1AndKu3RequestForm ku1AndKu3RequestForm = createKu1Form();

        try {
            if (!thaiRadio.isSelected() && !interRadio.isSelected()) {
                throw new IllegalArgumentException("กรุณาเลือกประเภทหลักสูตร");
            } else if (thaiRadio.isSelected()) {
                ku1AndKu3RequestForm.setCurriculum("Thai");
            } else {
                ku1AndKu3RequestForm.setCurriculum("International");
            }
            ku1AndKu3RequestForm.setTel(telTextField.getText());
            if (!firstRadio.isSelected() && !secondRadio.isSelected() && !summerRadio.isSelected()) {
                throw new IllegalArgumentException("กรุณาเลือกภาคการศึกษา");
            } else if (firstRadio.isSelected()) {
                ku1AndKu3RequestForm.setSemester("First");
            } else if (secondRadio.isSelected()) {
                ku1AndKu3RequestForm.setSemester("Second");
            } else {
                ku1AndKu3RequestForm.setSemester("Summer");
            }
            ku1AndKu3RequestForm.setYear(yearTextField.getText());
            ku1AndKu3RequestForm.setCampus(campusTextField.getText());

            ArrayList<String> subject = new ArrayList<>(), idSubject = new ArrayList<>();
            ArrayList<String> registerType = new ArrayList<>(), credit = new ArrayList<>();
            ArrayList<String> section = new ArrayList<>(), sectionType = new ArrayList<>();
            ArrayList<String> teacher = new ArrayList<>();
            VBox vBox = null;

            for (int i = 0; i < subjectVbox.getChildren().size(); i++) {
                if (i == 0) {
                    vBox = (VBox)subjectVbox.lookup(".subjectGroup");
                } else {
                    vBox = (VBox)subjectVbox.lookup(".subjectGroup" + i);
                }
                idSubject.add(((TextField)vBox.lookup(".subjectId")).getText());
                subject.add(((TextField)vBox.lookup(".subjectName")).getText());
                if (!((RadioButton)vBox.lookup(".creditCheck")).isSelected() && !((RadioButton)vBox.lookup(".auditCheck")).isSelected()) {
                    throw new IllegalArgumentException("กรุณาเลือกประเภทการลงทะเบียนในวิชาที่ " + (i + 1));
                } else if (((RadioButton)vBox.lookup(".creditCheck")).isSelected()) {
                    registerType.add("credit");
                } else {
                    registerType.add("audit");
                }
                credit.add(((TextField)vBox.lookup(".credit")).getText());
                section.add(((TextField)vBox.lookup(".section")).getText());
                if (!((RadioButton)vBox.lookup(".lecture")).isSelected() && !((RadioButton)vBox.lookup(".lab")).isSelected()) {
                    throw new IllegalArgumentException("กรุณาเลือกประเภทหมู่เรียนในวิชาที่ " + (i + 1));
                } else if (((RadioButton)vBox.lookup(".lecture")).isSelected()) {
                    sectionType.add("บรรยาย");
                } else {
                    sectionType.add("ปฏิบัติการ");
                }
                teacher.add(((TextField)vBox.lookup(".teacher")).getText());
            }
            ku1AndKu3RequestForm.addSubject(subject, idSubject, registerType, credit, section, sectionType, teacher);
            showConfirmPane(registerForm, ku1AndKu3RequestForm);
        } catch (IllegalArgumentException e) {
            ShowPopupRequest.showErrorPopup(e.getMessage());
        }
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    public void setLoginUser(User loginUser) {this.loginUser = loginUser;}

    @FXML
    public void onBackButtonClick() {
        try {
            String viewPath = "/ku/cs/views/register-request-form-pane.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            RegisterRequestFormController controller = fxmlLoader.getController();
            controller.setBorderPane(this.borderPane);
            controller.setLoginUser(loginUser);
            borderPane.setCenter(pane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onRemoveSubjectClick() {
        ObservableList<Node> node = subjectVbox.getChildren();
        if (node.size() > 1) {
            node.removeLast();
            amountSubject--;
        }
    }
    private Ku1AndKu3RequestForm createKu1Form() {
        return new Ku1AndKu3RequestForm(loginUser.getUUID(), loginUser.getName(), loginUser.getId(), "KU1");
    }

    private void showConfirmPane(RegisterRequestForm registerRequestForm, Ku1AndKu3RequestForm ku1AndKu3RequestForm) {
        ShowPopupRequest.showConfirmPopup(this.borderPane, registerRequestForm, ku1AndKu3RequestForm, loginUser);
    }
}
