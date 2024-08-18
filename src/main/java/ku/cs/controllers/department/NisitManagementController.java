package ku.cs.controllers.department;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ku.cs.controllers.department.components.*;

public class NisitManagementController {
    @FXML private Label pageTitleLabel;

    @FXML private VBox nisitTableVBox;
    @FXML private HBox tableHeaderHBox;

    @FXML private Label tableViewLabel;
    @FXML private TableView<?> nisitTableView;
    @FXML private TextField searchTextFiled;

    @FXML private VBox nisitEditorVBox;
    @FXML private ImageView nisitImageView;
    @FXML private TextField nisitFirstnameTextField;
    @FXML private TextField nisitLastnameTextField;
    @FXML private TextField nisitIdTextField;
    @FXML private Button editButton;

    @FXML private Button addNisitButton;
    @FXML private Button backButton;

    private CropImage nisitImage;
    private boolean editMode;

    @FXML public void initialize() {
        initLabel();
        initButton();
        nisitImage = new SquareImage(nisitImageView);
        nisitImage.setClipImage(50,50);
        nisitImage.setImage(new Image(getClass().getResourceAsStream("/images/profile-test.png")));
        this.editMode = true;
        toggleEditFiled();
    }
    private void initLabel(){
        new DefaultLabel(pageTitleLabel);
        new DefaultLabel(tableViewLabel);
    }
    private void initButton(){
        new RouteButton(backButton,"department-staff-request-list","transparent","#a6a6a6","#000000");
        new DefaultButton(addNisitButton,"#ABFFA4","#80BF7A","#000000").changeBackgroundRadius(15);
    }
    private void toggleEditFiled(){
        editMode = !editMode;
        System.out.println(editMode);
        boolean editable = editMode;
        String backgroundColor = editable ? "white" : "transparent";
        String buttonColor = editable ? "#ABFFA4" : "#FFA4A4";
        String buttonHoverColor = editable ? "#80BF7A" : "#E19494";
        String buttonLabelColor = editable ? "#000000" : "#000000";
        String buttonLabel = editable ? "Save" : "Edit";

        nisitEditorVBox.getChildren().forEach(node -> {
            if(node instanceof HBox){
                HBox hbox = (HBox) node;
                if(hbox.getChildren().get(0) instanceof TextField){
                    VBox.setMargin(hbox,new Insets(30,0,0,0));
                    hbox.setSpacing(10);
                    hbox.getChildren().forEach(child->{
                        if(child instanceof TextField){
                            TextField textField = (TextField) child;
                            textField.setStyle("-fx-background-color: "+backgroundColor+";"
                            +"-fx-background-radius: 10");
                            textField.setEditable(editable);
                        }
                    });
                }else if(hbox.getChildren().get(0) instanceof Button){
                    VBox.setMargin(hbox,new Insets(30,0,0,0));
                    hbox.getChildren().forEach(child->{
                        if(child instanceof Button){
                            Button button = (Button) child;
                            DefaultButton buttonEditor = new DefaultButton(button,buttonColor,buttonHoverColor,buttonLabelColor){
                              @Override
                              protected void handleClickEvent(){
                                  button.setOnMouseClicked(e -> {
                                      toggleEditFiled();
                                  });
                              }
                            };
                            buttonEditor.changeText(buttonLabel);
                            buttonEditor.changeBackgroundRadius(20);


                        }
                    });
                }
            }
        });
    }
    private void toggleEditFieldEnable(){

    }

}
