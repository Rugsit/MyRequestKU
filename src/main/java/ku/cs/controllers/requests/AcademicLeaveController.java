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

import java.io.IOException;

public class AcademicLeaveController {
    @FXML
    public BorderPane borderPane;

    int amountSubject;

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

    @FXML
    void onBackButtonClick() {
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
    void onRemoveSubjectClick() {
        if (registerRadio.isSelected() && subjectVbox.getChildren().size() > 1) {
            subjectVbox.getChildren().removeLast();
            amountSubject--;
        }
    }
}

