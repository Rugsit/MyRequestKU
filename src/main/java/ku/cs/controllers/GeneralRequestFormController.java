package ku.cs.controllers;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.models.GeneralRequestForm;

import java.io.IOException;

public class GeneralRequestFormController {

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
    public void onCreateGeneralForm() {
        GeneralRequestForm generalRequestForm = new GeneralRequestForm();
        try {
            generalRequestForm.setTel(telTextField.getText());
            if (degreeCerCheckBox.isSelected()) {
                if (!lostRadio.isSelected() && !damagedRadio.isSelected()) {
                    throw new IllegalArgumentException("ใบแทนปริญญาบัตรคุณต้องเลือก ชำรุด หรือ สูญหาย");
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
                    scene.getStylesheets().add(getClass().getResource("/ku/cs/styles/error-page-style.css").toExternalForm());
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

}
