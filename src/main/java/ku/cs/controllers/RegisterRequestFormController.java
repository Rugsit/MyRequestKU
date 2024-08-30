package ku.cs.controllers;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.models.RegisterRequestForm;

import java.io.IOException;

public class RegisterRequestFormController {
    @FXML
    private VBox buttonCOntainerVbox;
    @FXML
    private Stage currentErrorStage;

    @FXML
    private RadioButton addDropRadio;

    @FXML
    private RadioButton firstSemesRadio;

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
    private TextField newCredit;

    @FXML
    private TextField oldCredit;

    @FXML
    private TextArea otherTextArea;

    @FXML
    private RadioButton registerBelowNineRadio;

    @FXML
    private RadioButton registerRadio;

    @FXML
    private RadioButton secondSemesRadio;

    @FXML
    private RadioButton summerSemesRadio;

    @FXML
    private RadioButton transferFalRadio;

    @FXML
    private TextField yearTextField;

    @FXML
    private TextField newFalTextField;

    @FXML
    private TextField oldFalTextField;

    @FXML
    public BorderPane borderPane;

    @FXML
    private Button nextFormButton;

    @FXML
    private Button createFormButton;

    @FXML
    public void initialize() {
        showButtonWhenClick(lateRegistrationRadio, nextFormButton);
        showButtonWhenClick(addDropRadio, nextFormButton);
        showButtonWhenClick(registerRadio, nextFormButton);
        showButtonWhenClick(registerBelowNineRadio, createFormButton);
        showButtonWhenClick(latePaymentRadio, createFormButton);
        showButtonWhenClick(transferFalRadio, createFormButton);

        bindRadio(firstSemesRadio, registerRadio);
        bindRadio(secondSemesRadio, registerRadio);
        bindRadio(summerSemesRadio, registerRadio);
        bindRadio(yearTextField, registerRadio);
        bindRadio(oldCredit, registerRadio);
        bindRadio(newCredit, registerRadio);

        bindRadio(latePayFirstSemesRadio, latePaymentRadio);
        bindRadio(latePaySecondSemesRadio, latePaymentRadio);
        bindRadio(latePaySummerSemesRadio, latePaymentRadio);
        bindRadio(latePayYearTextField, latePaymentRadio);

        bindRadio(oldFalTextField, transferFalRadio);
        bindRadio(newFalTextField, transferFalRadio);
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    private void showButtonWhenClick (Control control, Button button) {
        if (control instanceof RadioButton) {
            RadioButton radioButton = (RadioButton) control;
            radioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    buttonCOntainerVbox.getChildren().clear();
                    buttonCOntainerVbox.getChildren().add(button);
                    button.setVisible(true);
                }
            });
        }

    }
    private void bindRadio(Control control, RadioButton radioButton) {
        if (control instanceof TextField) {
            TextField controlNew = (TextField) control;
            controlNew.disableProperty().bind(Bindings.not(radioButton.selectedProperty()));
            controlNew.disableProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    controlNew.setText("");
                }
            });
        } else if (control instanceof RadioButton) {
            RadioButton controlNew = (RadioButton) control;
            controlNew.disableProperty().bind(Bindings.not(radioButton.selectedProperty()));
            controlNew.disableProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    controlNew.setSelected(false);
                }
            });
        }
    }

    @FXML
    public void onBackButtonClick() {
        try {
            String viewPath = "/ku/cs/views/choose-request-form-pane.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            ChooseRequestFromController controller = fxmlLoader.getController();
            controller.setBorderPane(this.borderPane);
            borderPane.setCenter(pane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onCreateRegisterForm() {
        RegisterRequestForm registerRequestForm = new RegisterRequestForm();
        try {
            if (lateRegistrationRadio.isSelected()) {
                registerRequestForm.setLateRegister(true);
            } else if (addDropRadio.isSelected()) {
                registerRequestForm.setAddDrop(true);
            } else if (registerRadio.isSelected()) {
                if (!firstSemesRadio.isSelected() &&
                        !secondSemesRadio.isSelected() &&
                        !summerSemesRadio.isSelected()) {
                    throw new IllegalArgumentException("คุณต้องเลือกภาคการศึกษา ต้น ปลาย ฤดูร้อน");
                }
                if (firstSemesRadio.isSelected()) {
                    registerRequestForm.setSemester("ต้น");
                } else if (secondSemesRadio.isSelected()) {
                    registerRequestForm.setSemester("ปลาย");
                } else if (summerSemesRadio.isSelected()) {
                    registerRequestForm.setSemester("ฤดูร้อน");
                }
                registerRequestForm.setSemesterYear(yearTextField.getText());
                registerRequestForm.setOldCredit(oldCredit.getText());
                registerRequestForm.setNewCredit(newCredit.getText());
            } else if (registerBelowNineRadio.isSelected()) {
                registerRequestForm.setRegisterLessThan9(true);
            } else if (latePaymentRadio.isSelected()) {
                if (!latePayFirstSemesRadio.isSelected() &&
                        !latePaySecondSemesRadio.isSelected() &&
                        !latePaySummerSemesRadio.isSelected()) {
                    throw new IllegalArgumentException("คุณต้องเลือกภาคการศึกษา ต้น ปลาย ฤดูร้อน");
                }
                if (latePayFirstSemesRadio.isSelected()) {
                    registerRequestForm.setLatePaymentSemester("ต้น");
                } else if (latePaySecondSemesRadio.isSelected()) {
                    registerRequestForm.setLatePaymentSemester("ปลาย");
                } else if (latePaySummerSemesRadio.isSelected()) {
                    registerRequestForm.setLatePaymentSemester("ฤดูร้อน");
                }
                registerRequestForm.setLatePaymentYear(latePayYearTextField.getText());
            } else if (transferFalRadio.isSelected()) {
                registerRequestForm.setTransferFaculty(true);
                registerRequestForm.setOldFaculty(oldFalTextField.getText());
                registerRequestForm.setNewFaculty(newFalTextField.getText());
            }
            registerRequestForm.setSince(otherTextArea.getText());
        } catch (IllegalArgumentException ee) {
            try {
                if (currentErrorStage == null || !currentErrorStage.isShowing()) {
                    currentErrorStage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/error-page.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    ErrorGeneralRequestFormController errorGeneralRequestFormController = fxmlLoader.getController();
                    errorGeneralRequestFormController.setErrorMessage(ee.getMessage());
                    ErrorGeneralRequestFormController controller = fxmlLoader.getController();
                    controller.setStage(this.currentErrorStage);
                    scene.getStylesheets().add(getClass().getResource("/ku/cs/styles/error-page-style.css").toExternalForm());
                    currentErrorStage.setScene(scene);
                    currentErrorStage.initModality(Modality.APPLICATION_MODAL);
                    currentErrorStage.setTitle("Error");
                    currentErrorStage.show();
                }
            } catch (IOException eee) {
                System.err.println("Error: " + eee.getMessage());
            }
        }
    }
}
