package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class GeneralRequestFormController {
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
}
