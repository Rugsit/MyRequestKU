package ku.cs.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.models.Ku1RequestForm;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Ku1FormController {
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

    int amountSubject;

    @FXML
    public BorderPane borderPane;

    @FXML
    private VBox subjectVbox;

    @FXML
    private VBox prototypeVbox;

    @FXML
    private TextField teacherTextField;
    @FXML
    public void addSubject() {
        subjectVbox.getChildren().add(deepCopyVbox(prototypeVbox));
        amountSubject++;
    }

    @FXML
    public void initialize() {
        amountSubject = 1;
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
        Ku1RequestForm ku1RequestForm = new Ku1RequestForm();

        try {
            if (!thaiRadio.isSelected() && !interRadio.isSelected()) {
                throw new IllegalArgumentException("กรุณาเลือกประเภทหลักสูตร");
            } else if (thaiRadio.isSelected()) {
                ku1RequestForm.setCurriculum("thai");
            } else {
                ku1RequestForm.setCurriculum("international");
            }
            ku1RequestForm.setTel(telTextField.getText());
            if (!firstRadio.isSelected() && !secondRadio.isSelected() && !summerRadio.isSelected()) {
                throw new IllegalArgumentException("กรุณาเลือกภาคการศึกษา");
            } else if (firstRadio.isSelected()) {
                ku1RequestForm.setSemester("first");
            } else if (secondRadio.isSelected()) {
                ku1RequestForm.setSemester("second");
            } else {
                ku1RequestForm.setSemester("summer");
            }
            ku1RequestForm.setYear(yearTextField.getText());
            ku1RequestForm.setCampus(campusTextField.getText());

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
                    sectionType.add("lecture");
                } else {
                    sectionType.add("lab");
                }
                teacher.add(((TextField)vBox.lookup(".teacher")).getText());
            }
            ku1RequestForm.addSubject(subject, idSubject, registerType, credit, section, sectionType, teacher);
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
            } catch (IOException eee) {
                System.err.println("Error: " + eee.getMessage());
            }
        }
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    @FXML
    public void onBackButtonClick() {
        try {
            String viewPath = "/ku/cs/views/register-request-form-pane.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            RegisterRequestFormController controller = fxmlLoader.getController();
            controller.setBorderPane(this.borderPane);
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
}
