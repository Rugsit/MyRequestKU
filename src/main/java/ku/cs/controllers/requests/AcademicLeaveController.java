package ku.cs.controllers.requests;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.models.request.AcademicLeaveRequestForm;
import ku.cs.models.user.User;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class AcademicLeaveController {
    private int amountSubject;
    private User loginUser;

    // FXML Component
    @FXML
    private Stage currentConfirmStage;
    @FXML
    private Stage currentErrorStage;
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
    public void initialize() {
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

    public void setErrorStage(Stage currentErrorStage) {
        this.currentErrorStage = currentErrorStage;
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
            form.setYearTo(fromYearTextField.getText());
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
            try {
                if (currentErrorStage == null || !currentErrorStage.isShowing()) {
                    currentErrorStage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/error-page.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    ErrorGeneralRequestFormController errorGeneralRequestFormController = fxmlLoader.getController();
                    errorGeneralRequestFormController.setErrorMessage(e.getMessage());
                    ErrorGeneralRequestFormController controller = fxmlLoader.getController();
                    controller.setStage(this.currentErrorStage);
                    scene.getStylesheets().add(getClass().getResource("/ku/cs/styles/error-confirm-edit-page-style.css").toExternalForm());
                    currentErrorStage.setScene(scene);
                    currentErrorStage.initModality(Modality.APPLICATION_MODAL);
                    currentErrorStage.setTitle("Error");
                    currentErrorStage.show();
                }
            } catch (IOException ee) {
                System.err.println("Error: " + ee.getMessage());
            }
        }
    }

    private AcademicLeaveRequestForm createAcademicLeaveRequestForm() {
        return new AcademicLeaveRequestForm(loginUser.getUUID(), loginUser.getName(), loginUser.getId(), "ลาพักการศึกษา");
    }

    private void showConfirmPane(AcademicLeaveRequestForm academicLeaveRequestForm) {
        try {
            if (currentConfirmStage == null || !currentConfirmStage.isShowing()) {
                currentConfirmStage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/confirm-page.fxml"));
                Scene scene = new Scene(fxmlLoader.load());

                ConfirmRequestFormController controller = fxmlLoader.getController();
                controller.setStage(this.currentConfirmStage);
                controller.setBorderPane(this.borderPane);
                controller.setRequestForm(academicLeaveRequestForm);
                controller.setLoginUser(loginUser);
                scene.getStylesheets().add(getClass().getResource("/ku/cs/styles/error-confirm-edit-page-style.css").toExternalForm());
                currentConfirmStage.setScene(scene);
                currentConfirmStage.initModality(Modality.APPLICATION_MODAL);
                currentConfirmStage.setTitle("Confirm");
                currentConfirmStage.show();
            }
        } catch (IOException ee) {
            System.err.println("Error: " + ee.getMessage());
        }
    }
}

