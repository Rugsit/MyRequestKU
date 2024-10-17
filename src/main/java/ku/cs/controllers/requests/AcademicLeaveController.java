package ku.cs.controllers.requests;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ku.cs.models.request.AcademicLeaveRequestForm;
import ku.cs.models.user.User;
import ku.cs.services.SetTransition;
import ku.cs.services.ShowPopupRequest;

import java.io.IOException;
import java.util.ArrayList;

public class AcademicLeaveController {
    private int amountSubject;
    private User loginUser;

    // FXML Component
    @FXML
    private BorderPane borderPane;
    @FXML
    private TextArea addressTextArea;
    @FXML
    private TextField amountLeaveTextField;
    @FXML
    private RadioButton fromFirstSemesterRadio;
    @FXML
    private RadioButton fromSecondSemesterRadio;
    @FXML
    private TextField fromYearTextField;
    @FXML
    private RadioButton registerFirstSemester;
    @FXML
    private RadioButton registerRadio;
    @FXML
    private RadioButton registerSecondSemester;
    @FXML
    private TextField registerYearTextField;
    @FXML
    private TextArea sinceLeaveTextArea;
    @FXML
    private TextField telTextField;
    @FXML
    private RadioButton toFirstSemesterRadio;
    @FXML
    private RadioButton toSecondSemesterRadio;
    @FXML
    private TextField toYearTextField;
    @FXML
    private VBox subjectVbox;
    @FXML
    private HBox subjectHbox;
    @FXML
    private Button backButton;
    @FXML
    private Button addButton;
    @FXML
    private Button removeButton;
    @FXML
    private Button createFormButton;


    @FXML
    public void initialize() {
        SetTransition.setButtonBounce(backButton);
        SetTransition.setButtonBounce(addButton);
        SetTransition.setButtonBounce(removeButton);
        SetTransition.setButtonBounce(createFormButton);
        amountSubject = 1;
        bindRegister(registerRadio, registerFirstSemester);
        bindRegister(registerRadio, registerSecondSemester);
        bindRegister(registerRadio, registerYearTextField);
        subjectVbox.disableProperty().bind(Bindings.not(registerRadio.selectedProperty()));
    }

    private void bindRegister(RadioButton radioButton, Control control) {
        if (control instanceof TextField) {
            TextField field = (TextField) control;
            field.disableProperty().bind(Bindings.not(radioButton.selectedProperty()));
            field.disableProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    field.setText("");
                }
            });
        } else if (control instanceof RadioButton) {
            RadioButton rb = (RadioButton) control;
            rb.disableProperty().bind(Bindings.not(radioButton.selectedProperty()));
            rb.disableProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    rb.setSelected(false);
                }
            });
        }
    }

    private HBox deepCopyHBox(HBox hbox) {
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
                newHbox.getChildren().add(newLabel);
            } else if (node instanceof TextField) {
                TextField textField = (TextField) node;
                TextField newTextField = new TextField();
                newTextField.setPrefHeight(textField.getPrefHeight());
                newTextField.setPrefWidth(textField.getPrefWidth());
                HBox.setMargin(newTextField, HBox.getMargin(textField));
                newTextField.getStyleClass().addAll(textField.getStyleClass());
                newHbox.getChildren().add(newTextField);
            }
        }
        return newHbox;
    }

    @FXML
    void addSubject() {
        if (registerRadio.isSelected()) {
            HBox newHbox = deepCopyHBox(subjectHbox);
            subjectVbox.getChildren().add(newHbox);
            amountSubject++;
        }
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    public void setLoginUser(User loginUser) {this.loginUser = loginUser;}

    @FXML
    void onBackButtonClick() {
        try {
            String viewPath = "/ku/cs/views/choose-request-form-pane.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            ChooseRequestFromController controller = fxmlLoader.getController();
            controller.setBorderPane(this.borderPane);
            controller.setLoginUser(loginUser);
            borderPane.setCenter(pane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onRemoveSubjectClick() {
        if (registerRadio.isSelected() && subjectVbox.getChildren().size() > 1) {
            subjectVbox.getChildren().removeLast();
            amountSubject--;
        }
    }

    @FXML
    public void onCreateRegisterForm() {
        AcademicLeaveRequestForm form = createAcademicLeaveRequestForm();
        try {
            form.setTel(telTextField.getText());
            form.setAddress(addressTextArea.getText());
            form.setReason(sinceLeaveTextArea.getText());
            form.setAmountSemester(amountLeaveTextField.getText());
            if (!fromFirstSemesterRadio.isSelected() && !fromSecondSemesterRadio.isSelected()) {
                throw new IllegalArgumentException("กรุณาเลือกภาคการศึกษา");
            }
            if (fromFirstSemesterRadio.isSelected()){
               form.setSemesterFrom("First");
            } else {
                form.setSemesterFrom("Second");
            }
            form.setYearFrom(fromYearTextField.getText());

            if (!toFirstSemesterRadio.isSelected() && !toSecondSemesterRadio.isSelected()) {
                throw new IllegalArgumentException("กรุณาเลือกภาคการศึกษา");
            }
            if (toFirstSemesterRadio.isSelected()){
                form.setSemesterTo("First");
            } else {
                form.setSemesterTo("Second");
            }
            form.setYearTo(toYearTextField.getText());
            if (registerRadio.isSelected()) {
                form.setHaveRegister(registerRadio.isSelected());
                if (!registerFirstSemester.isSelected() && !registerSecondSemester.isSelected()) {
                    throw new IllegalArgumentException("กรุณาเลือกภาคการศึกษา");
                }
                if (registerFirstSemester.isSelected()){
                    form.setHaveRegisterSemester("First");
                } else {
                    form.setHaveRegisterSemester("Second");
                }
                form.setGetHaveRegisterYear(registerYearTextField.getText());
                ArrayList<String> subjectId = new ArrayList<>();
                ArrayList<String> teacher = new ArrayList<>();
                for (Node node : subjectVbox.getChildren()) {
                    HBox hBox = (HBox) node;
                    TextField textSubjectId = (TextField) hBox.lookup(".subjectId");
                    subjectId.add(textSubjectId.getText().trim());
                    TextField textTeacher = (TextField) hBox.lookup(".teacher");
                    teacher.add(textTeacher.getText().trim());
                }
                form.addSubject(subjectId, teacher);
            }
            showConfirmPane(form);
        } catch (IllegalArgumentException e) {
            ShowPopupRequest.showErrorPopup(e.getMessage());
        }
    }

    private AcademicLeaveRequestForm createAcademicLeaveRequestForm() {
        return new AcademicLeaveRequestForm(loginUser.getUUID(), loginUser.getName(), loginUser.getId(), "ลาพักการศึกษา");
    }

    private void showConfirmPane(AcademicLeaveRequestForm academicLeaveRequestForm) {
        ShowPopupRequest.showConfirmPopup(this.borderPane, academicLeaveRequestForm, null, loginUser);
    }
}

