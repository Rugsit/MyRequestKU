package ku.cs.controllers.department;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import ku.cs.controllers.department.components.*;

import java.util.ListIterator;
import java.util.Scanner;

public class NisitManagementController {
    @FXML private Label pageTitleLabel;

    @FXML private VBox nisitTableVBox;
    @FXML private HBox tableHeaderHBox;

    @FXML private Label tableViewLabel;
    @FXML private TableView<?> nisitTableView;
    @FXML private TextField searchTextFiled;

    @FXML private VBox nisitEditorVBox;
    @FXML private ImageView nisitImageView;

    private TextFieldStack nisitFirstnameTextField;
    private TextFieldStack nisitLastnameTextField;
    private TextFieldStack nisitIdTextField;
    private Button editButton;

    @FXML private Button addNisitButton;
    @FXML private Button backButton;

    private CropImage nisitImage;
    private double editorHBoxWidth;
    private double editorHBoxHeight;

    private boolean editMode;

    @FXML public void initialize() {
        this.editMode = true;
        this.editorHBoxWidth = 270;
        this.editorHBoxHeight = 50;

        initLabel();
        initButton();
        nisitImage = new SquareImage(nisitImageView);
        nisitImage.setClipImage(50,50);
        nisitImage.setImage(new Image(getClass().getResourceAsStream("/images/profile-test.png")));
        initNisitEditor();

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
    private void initNisitEditor(){
        ObservableList<Node> children =  nisitEditorVBox.getChildren();

        HBox container = newEditorContainerHBox();
        container.getChildren().add(nisitFirstnameTextField = new TextFieldStack("FName"));
        container.getChildren().add(nisitLastnameTextField = new TextFieldStack("LName"));
        children.add(container);
        container = newEditorContainerHBox();
        container.getChildren().add(nisitIdTextField = new TextFieldStack("Id"));
        children.add(container);
        container = newEditorContainerHBox();
        container.getChildren().add(editButton = new Button());
        children.add(container);

    }
    private HBox newEditorContainerHBox(){
        double w = editorHBoxWidth;
        double h = editorHBoxHeight;
        HBox container = new HBox();
        container.setPrefSize(w,h);
        return container;
    }
    private void toggleEditFiled(){
        editMode = !editMode;
        System.out.println(editMode);
        boolean editable = editMode;
        String buttonColor = editable ? "#ABFFA4" : "#FFA4A4";
        String buttonHoverColor = editable ? "#80BF7A" : "#E19494";
        String buttonLabelColor = editable ? "#000000" : "#000000";
        String buttonLabel = editable ? "บันทึก" : "แก้ไข";

        nisitEditorVBox.getChildren().forEach(node -> {
            if(node instanceof HBox){
                HBox hbox = (HBox) node;
                hbox.setSpacing(20);
                VBox.setMargin(hbox,new Insets(30,0,0,0));
                for(int i = 0;i < hbox.getChildren().size();i++){
                    Node child = hbox.getChildren().get(i);
                    if(child instanceof TextFieldStack){
                        TextFieldStack t = (TextFieldStack) child;
                        t.toggleTextField(editMode);
//                        if(i == 0){
//                            HBox.setMargin(child,new Insets(0,0,0,0));
//                        }
                    }else if(child instanceof Button){
                        hbox.setAlignment(Pos.CENTER);
                        Button button = (Button) child;
                        DefaultButton b = new DefaultButton(button,buttonColor,buttonHoverColor,buttonLabelColor){
                            @Override
                            protected void handleClickEvent(){
                                button.setOnMouseClicked(e -> {
                                    toggleEditFiled();
                                });
                            }
                        };
                        b.changeText(buttonLabel,28, FontWeight.NORMAL);
                        b.changeBackgroundRadius(20);
                    }
                }
            }
        });
    }

}
