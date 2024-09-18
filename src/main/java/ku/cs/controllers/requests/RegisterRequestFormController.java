package ku.cs.controllers.requests;

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
import ku.cs.models.request.RegisterRequestForm;
import ku.cs.models.user.User;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

public class RegisterRequestFormController {
    User loginUser;

    @FXML
    private Stage currentConfirmStage;
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

    public void setLoginUser(User loginUser) {this.loginUser = loginUser;}

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
            controller.setLoginUser(loginUser);
            borderPane.setCenter(pane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onCreateRegisterForm() {
        RegisterRequestForm registerRequestForm = createRegisterForm();
        try {
            if (registerBelowNineRadio.isSelected()) {
                registerRequestForm.setRegisterLessThan9(true);
            } else if (latePaymentRadio.isSelected()) {
                if (!latePayFirstSemesRadio.isSelected() &&
                        !latePaySecondSemesRadio.isSelected() &&
                        !latePaySummerSemesRadio.isSelected()) {
                    throw new IllegalArgumentException("กรุณาเลือกภาคการศึกษา ต้น ปลาย ฤดูร้อน");
                }
                if (latePayFirstSemesRadio.isSelected()) {
                    registerRequestForm.setLatePaymentSemester("First");
                } else if (latePaySecondSemesRadio.isSelected()) {
                    registerRequestForm.setLatePaymentSemester("Second");
                } else if (latePaySummerSemesRadio.isSelected()) {
                    registerRequestForm.setLatePaymentSemester("Summer");
                }
                registerRequestForm.setLatePaymentYear(latePayYearTextField.getText());
            } else if (transferFalRadio.isSelected()) {
                registerRequestForm.setTransferFaculty(true);
                registerRequestForm.setOldFaculty(oldFalTextField.getText());
                registerRequestForm.setNewFaculty(newFalTextField.getText());
            }
            registerRequestForm.setSince(otherTextArea.getText());

            showConfirmPane(registerRequestForm);
        } catch (IllegalArgumentException ee) {
            showErrorPopup(ee);
        }
    }

    @FXML
    public void onLateRegisterClick() {
        nextFormButton.setOnAction(e -> {
            try {
                RegisterRequestForm registerRequestForm = createRegisterForm();
                registerRequestForm.setLateRegister(true);
                registerRequestForm.setSince(otherTextArea.getText());

                String viewPath = "/ku/cs/views/ku1-form-pane.fxml";
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource(viewPath));
                Pane pane = fxmlLoader.load();
                Ku1FormController controller = fxmlLoader.getController();
                controller.setRegisterForm(registerRequestForm);
                controller.setBorderPane(this.borderPane);
                controller.setLoginUser(loginUser);
                borderPane.setCenter(pane);
            } catch (IOException ee) {
                throw new RuntimeException(ee);
            } catch (IllegalArgumentException ee) {
                showErrorPopup(ee);
            }
        });
    }

    @FXML
    public void onAddDropClick() {
        nextFormButton.setOnAction(e -> {
            try {
                RegisterRequestForm registerRequestForm = createRegisterForm();
                registerRequestForm.setAddDrop(true);
                registerRequestForm.setSince(otherTextArea.getText());

                goToKU3Form(registerRequestForm);
            } catch (IllegalArgumentException ee) {
                showErrorPopup(ee);
            }
        });
    }

    @FXML
    public void onRegisterAbove22Click() {
        nextFormButton.setOnAction(e -> {
            try {
                RegisterRequestForm registerRequestForm = createRegisterForm();
                registerRequestForm.setRegisterMoreThan22(true);
                if (registerRadio.isSelected()) {
                    if (!firstSemesRadio.isSelected() &&
                            !secondSemesRadio.isSelected() &&
                            !summerSemesRadio.isSelected()) {
                        throw new IllegalArgumentException("กรุณาเลือกภาคการศึกษา ต้น ปลาย ฤดูร้อน");
                    }
                    if (firstSemesRadio.isSelected()) {
                        registerRequestForm.setSemester("First");
                    } else if (secondSemesRadio.isSelected()) {
                        registerRequestForm.setSemester("Second");
                    } else if (summerSemesRadio.isSelected()) {
                        registerRequestForm.setSemester("Summer");
                    }
                    registerRequestForm.setSemesterYear(yearTextField.getText());
                    registerRequestForm.setOldCredit(oldCredit.getText());
                    registerRequestForm.setNewCredit(newCredit.getText());
                }
                registerRequestForm.setSince(otherTextArea.getText());
                goToKU3Form(registerRequestForm);
            } catch (IllegalArgumentException ee) {
                showErrorPopup(ee);
            }
        });
    }

    @FXML
    private void showErrorPopup(IllegalArgumentException ee) {
        try {
            if (currentErrorStage == null || !currentErrorStage.isShowing()) {
                currentErrorStage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/error-page.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                ErrorGeneralRequestFormController errorGeneralRequestFormController = fxmlLoader.getController();
                errorGeneralRequestFormController.setErrorMessage(ee.getMessage());
                ErrorGeneralRequestFormController controller = fxmlLoader.getController();
                controller.setStage(this.currentErrorStage);
                scene.getStylesheets().add(getClass().getResource("/ku/cs/styles/error-confirm-edit-page-style.css").toExternalForm());
                currentErrorStage.setScene(scene);
                currentErrorStage.initModality(Modality.APPLICATION_MODAL);
                currentErrorStage.setTitle("Error");
                currentErrorStage.show();
            }
        } catch (IOException eee) {
            System.err.println("Error: " + eee.getMessage());
        }
    }

    private RegisterRequestForm createRegisterForm() {
        UUID uuid = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        return new RegisterRequestForm(uuid,loginUser.getUUID(), loginUser.getName(), loginUser.getId(), now, now, "Register", "ใบคำร้องใหม่", "ส่งคำร้องต่อให้อาจารย์ที่ปรึกษา");
    }

    private void showConfirmPane(RegisterRequestForm registerRequestForm) {
        try {
            if (currentConfirmStage == null || !currentConfirmStage.isShowing()) {
                currentConfirmStage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/confirm-page.fxml"));
                Scene scene = new Scene(fxmlLoader.load());

                ConfirmRequestFormController controller = fxmlLoader.getController();
                controller.setStage(this.currentConfirmStage);
                controller.setBorderPane(this.borderPane);
                controller.setRequestForm(registerRequestForm);
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

    private void goToKU3Form(RegisterRequestForm registerRequestForm) {
        String viewPath = "/ku/cs/views/ku3-form-pane.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(viewPath));
        Pane pane = null;
        try {
            pane = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Ku3FormController controller = fxmlLoader.getController();
        controller.setRegisterForm(registerRequestForm);
        controller.setBorderPane(this.borderPane);
        controller.setLoginUser(loginUser);
        borderPane.setCenter(pane);
    }
}
