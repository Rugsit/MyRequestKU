package ku.cs.controllers.requests;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.models.request.GeneralRequestForm;
import ku.cs.models.user.User;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

public class GeneralRequestFormController {
    private User loginUser;
    // FXML Component
    @FXML
    private Stage currentConfirmStage;
    @FXML
    private Stage currentErrorStage;
    @FXML
    private CheckBox changeNameCheckBox;
    @FXML
    private RadioButton damagedRadio;
    @FXML
    private CheckBox degreeCerCheckBox;
    @FXML
    private RadioButton lostRadio;
    @FXML
    private TextField newEngNameTextFeild;
    @FXML
    private TextField newEngSurNameTextFeild;
    @FXML
    private TextField newThaiNameTextFeild;
    @FXML
    private TextField newThaiSurNameTextFeild;
    @FXML
    private TextField oldEngNameTextFeild;
    @FXML
    private TextField oldEngSurNameTextFeild;
    @FXML
    private TextField oldThaiNameTextFeild;
    @FXML
    private TextField oldThaiSurNameTextFeild;
    @FXML
    private CheckBox otherCheckBox;
    @FXML
    private TextArea otherTextArea;
    @FXML
    private CheckBox surNameCheckBox;
    @FXML
    private TextField telTextField;
    @FXML
    public BorderPane borderPane;

    @FXML
    public void initialize() {
        bindCheckBox(lostRadio, degreeCerCheckBox);
        bindCheckBox(damagedRadio, degreeCerCheckBox);

        bindCheckBox(oldThaiNameTextFeild, changeNameCheckBox);
        bindCheckBox(newThaiNameTextFeild, changeNameCheckBox);
        bindCheckBox(oldEngNameTextFeild, changeNameCheckBox);
        bindCheckBox(newEngNameTextFeild, changeNameCheckBox);

        bindCheckBox(oldThaiSurNameTextFeild, surNameCheckBox);
        bindCheckBox(newThaiSurNameTextFeild, surNameCheckBox);
        bindCheckBox(oldEngSurNameTextFeild, surNameCheckBox);
        bindCheckBox(newEngSurNameTextFeild, surNameCheckBox);

        bindCheckBox(otherTextArea, otherCheckBox);
    }

    private void bindCheckBox(Control control, CheckBox checkBox) {
        if (control instanceof TextField) {
            TextField controlNew = (TextField) control;
            controlNew.disableProperty().bind(Bindings.not(checkBox.selectedProperty()));
            controlNew.disableProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    controlNew.setText("");
                }
            });
        } else if (control instanceof RadioButton) {
            RadioButton controlNew = (RadioButton) control;
            controlNew.disableProperty().bind(Bindings.not(checkBox.selectedProperty()));
            controlNew.disableProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    controlNew.setSelected(false);
                }
            });
        } else if (control instanceof TextArea) {
            TextArea controlNew = (TextArea) control;
            controlNew.disableProperty().bind(Bindings.not(checkBox.selectedProperty()));
            controlNew.disableProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    controlNew.setText("");
                }
            });
        }
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }
    public void setLoginUser(User loginUser) {this.loginUser = loginUser;}

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
    public void onCreateGeneralForm() {
        GeneralRequestForm generalRequestForm = createGeneralForm();
        try {
            generalRequestForm.setTel(telTextField.getText());
            if (degreeCerCheckBox.isSelected()) {
                if (!lostRadio.isSelected() && !damagedRadio.isSelected()) {
                    throw new IllegalArgumentException("ใบแทนปริญญาบัตรกรุณาเลือก ชำรุด หรือ สูญหาย");
                }
                if (lostRadio.isSelected()) {
                    generalRequestForm.setDegreeCertificateLost(true);
                } else {
                    generalRequestForm.setDegreeCertificateDamage(true);
                }
            } if (changeNameCheckBox.isSelected()) {
                generalRequestForm.setOldThaiName(oldThaiNameTextFeild.getText());
                generalRequestForm.setNewThaiName(newThaiNameTextFeild.getText());
                generalRequestForm.setOldEngName(oldEngNameTextFeild.getText());
                generalRequestForm.setNewEngName(newEngNameTextFeild.getText());
            } if (surNameCheckBox.isSelected()) {
                generalRequestForm.setOldThaiSurName(oldThaiSurNameTextFeild.getText());
                generalRequestForm.setNewThaiSurName(newThaiSurNameTextFeild.getText());
                generalRequestForm.setOldEngSurName(oldEngSurNameTextFeild.getText());
                generalRequestForm.setNewEngSurName(newEngSurNameTextFeild.getText());
            } if (otherCheckBox.isSelected()) {
                generalRequestForm.setOthers(otherTextArea.getText());
            }
            showConfirmPane(generalRequestForm);
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

    public void setErrorStage(Stage currentErrorStage) {
        this.currentErrorStage = currentErrorStage;
    }

    private GeneralRequestForm createGeneralForm() {
        return new GeneralRequestForm(loginUser.getUUID(), loginUser.getName(), loginUser.getId(), "ทั่วไป");
    }

    private void showConfirmPane(GeneralRequestForm generalRequestForm) {
        try {
            if (currentConfirmStage == null || !currentConfirmStage.isShowing()) {
                currentConfirmStage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/confirm-page.fxml"));
                Scene scene = new Scene(fxmlLoader.load());

                ConfirmRequestFormController controller = fxmlLoader.getController();
                controller.setStage(this.currentConfirmStage);
                controller.setBorderPane(this.borderPane);
                controller.setRequestForm(generalRequestForm);
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
