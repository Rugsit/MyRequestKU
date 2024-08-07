package ku.cs.controllers.department;

import javafx.fxml.FXML;
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
    @FXML private TextField nisitNameTextField;

    @FXML private Button addNisitButton;
    @FXML private Button backButton;

    private CropImage nisitImage;

    @FXML public void initialize() {
        initLabel();
        initButton();
        nisitImage = new SquareImage(nisitImageView);
        nisitImage.setClipImage(50,50);
        nisitImage.setImage(new Image(getClass().getResourceAsStream("/images/profile-test.png")));
    }
    private void initLabel(){
        new DefaultLabel(pageTitleLabel);
        new DefaultLabel(tableViewLabel);
    }
    private void initButton(){
        new RouteButton(backButton,"department-staff-request-list","transparent","#a6a6a6","#000000");
        new DefaultButton(addNisitButton,"#ABFFA4","#80BF7A","#000000").changeBackgroundRadius(15);
    }

}
