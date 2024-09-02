package ku.cs.controllers;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
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
import ku.cs.models.Ku1AndKu3RequestForm;
import ku.cs.models.RegisterRequestForm;

import java.io.IOException;
import java.util.ArrayList;

public class Ku3FormController {
    RegisterRequestForm registerForm;
    @FXML
    private CheckBox part1Checkbox;

    @FXML
    private CheckBox part2CheckBox;

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
    private VBox subjectVbox;

    @FXML
    private VBox subjectDropVbox;

    @FXML
    private VBox prototypePart1Vbox;

    @FXML
    private VBox prototypePart2Vbox;

    int amountSubjectPart1;
    int amountSubjectPart2;

    @FXML
    public BorderPane borderPane;

    public void setRegisterForm(RegisterRequestForm registerForm) {
        this.registerForm = registerForm;
    }

    public void addSubjectPart1() {
        byte id = 1;
        if (!part1Checkbox.isSelected()) {return;}
        subjectVbox.getChildren().add(deepCopyVbox(prototypePart1Vbox, id));
        amountSubjectPart1++;
    }

    public void addSubjectPart2() {
        byte id = 2;
        if (!part2CheckBox.isSelected()) {return;}
        subjectDropVbox.getChildren().add(deepCopyVbox(prototypePart2Vbox, id));
        amountSubjectPart2++;
    }

    @FXML
    public void onRemoveSubjectPart1Click() {
        if (!part1Checkbox.isSelected()) {return;}
        ObservableList<Node> node = subjectVbox.getChildren();
        if (node.size() > 1) {
            node.removeLast();
            amountSubjectPart1--;
        }
    }
    @FXML
    public void onRemoveSubjectPart2Click() {
        if (!part2CheckBox.isSelected()) {return;}
        ObservableList<Node> node = subjectDropVbox.getChildren();
        if (node.size() > 1) {
            node.removeLast();
            amountSubjectPart2--;
        }
    }


    @FXML
    public void initialize() {
        amountSubjectPart1 = 1;
        amountSubjectPart2 = 2;
        subjectVbox.disableProperty().bind(Bindings.not(part1Checkbox.selectedProperty()));
        subjectDropVbox.disableProperty().bind(Bindings.not(part2CheckBox.selectedProperty()));

    }
    private VBox deepCopyVbox(VBox vbox, byte id) {
        VBox newVbox = new VBox();
        VBox.setMargin(newVbox, VBox.getMargin(vbox));
        if (id == 1) {newVbox.getStyleClass().add(vbox.getStyleClass().getFirst() + amountSubjectPart1);}
        else {newVbox.getStyleClass().add(vbox.getStyleClass().getFirst() + amountSubjectPart2);}
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
    public void onCreateRegisterForm() {
        Ku1AndKu3RequestForm ku1AndKu3RequestForm = new Ku1AndKu3RequestForm();

        try {
            if (!thaiRadio.isSelected() && !interRadio.isSelected()) {
                throw new IllegalArgumentException("กรุณาเลือกประเภทหลักสูตร");
            } else if (thaiRadio.isSelected()) {
                ku1AndKu3RequestForm.setCurriculum("thai");
            } else {
                ku1AndKu3RequestForm.setCurriculum("international");
            }
            ku1AndKu3RequestForm.setTel(telTextField.getText());
            if (!firstRadio.isSelected() && !secondRadio.isSelected() && !summerRadio.isSelected()) {
                throw new IllegalArgumentException("กรุณาเลือกภาคการศึกษา");
            } else if (firstRadio.isSelected()) {
                ku1AndKu3RequestForm.setSemester("first");
            } else if (secondRadio.isSelected()) {
                ku1AndKu3RequestForm.setSemester("second");
            } else {
                ku1AndKu3RequestForm.setSemester("summer");
            }
            ku1AndKu3RequestForm.setYear(yearTextField.getText());
            ku1AndKu3RequestForm.setCampus(campusTextField.getText());

            String group;
            for (int index = 0; index < 2; index++) {
                VBox targetVBox;
                if (index == 0 && !part1Checkbox.isSelected()) {continue;}
                else if (index == 1 && !part2CheckBox.isSelected()) {continue;}
                if (index == 0) {
                    group = "subjectPart1Group";
                    targetVBox = subjectVbox;
                } else {
                    group = "subjectPart2Group";
                    targetVBox = subjectDropVbox;
                }
                ArrayList<String> subject = new ArrayList<>(), idSubject = new ArrayList<>();
                ArrayList<String> registerType = new ArrayList<>(), credit = new ArrayList<>();
                ArrayList<String> section = new ArrayList<>(), sectionType = new ArrayList<>();
                ArrayList<String> teacher = new ArrayList<>();
                VBox vBox = null;

                for (int i = 0; i < targetVBox.getChildren().size(); i++) {
                    if (i == 0) {
                        vBox = (VBox)targetVBox.lookup("." + group);
                    } else {
                        vBox = (VBox)subjectVbox.lookup("." + group + i);
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
                if (index == 0) {
                    ku1AndKu3RequestForm.addSubject(subject, idSubject, registerType, credit, section, sectionType, teacher, (byte) 1);
                } else {
                    ku1AndKu3RequestForm.addSubject(subject, idSubject, registerType, credit, section, sectionType, teacher, (byte) 2);
                }

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
            } catch (IOException eee) {
                System.err.println("Error: " + eee.getMessage());
            }
            return;
        }

        System.out.println(ku1AndKu3RequestForm);
    }
}
