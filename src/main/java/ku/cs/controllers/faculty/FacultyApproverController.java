package ku.cs.controllers.faculty;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ku.cs.services.FXRouter;
import ku.cs.views.components.CropImage;
import ku.cs.views.components.DefaultButton;
import ku.cs.views.components.DefaultLabel;
import ku.cs.views.components.RouteButton;

import java.io.IOException;

public class FacultyApproverController {
    @FXML private Label pageTitleLabel;

    @FXML private HBox tableHeaderHBox;
    @FXML private Label tableViewLabel;
    @FXML private Button addApproverButton;
    @FXML private TableView approverTableView;

    @FXML private VBox imageEditorVBox;
    @FXML private ImageView approverImageView;
    @FXML private Label approverNameLabel;
    @FXML private Label approverPositionLabel;

    @FXML private HBox uploadHBox;
    @FXML private ImageView iconPdfImageView;
    @FXML private Label fileNameLabel;
    @FXML private Button removeFileButton;
    @FXML private ImageView iconRemoveFileImageView;

    @FXML private Button uploadFileButton;

    @FXML private Button backButton;
    @FXML
    public void initialize() {
        initLabel();
        initButton();
        initTableHeaderHBox();
        initImageEditorVBox();
        new CropImage(approverImageView).setClipImage(50,50);
    }
    private void initLabel(){
        new DefaultLabel(pageTitleLabel);
        new DefaultLabel(tableViewLabel);
        new DefaultLabel(approverNameLabel);
        new DefaultLabel(approverPositionLabel);
        new DefaultLabel(fileNameLabel);
    }
    private void initButton(){
        new RouteButton(backButton,"faculty-pagejru","transparent","#a6a6a6","#000000");
        new DefaultButton(addApproverButton,"#FFE0A4","#a6a6a6","#000000").changeBackgroundRadius(15);
        new DefaultButton(removeFileButton,"transparent","#a6a6a6","#000000");
        new DefaultButton(uploadFileButton,"#ABFFA4","#a6a6a6","#000000").changeBackgroundRadius(15);
    }
    private void initTableHeaderHBox(){
        tableHeaderHBox.setSpacing(20);
    }
    private void initImageEditorVBox(){
//        imageEditorVBox.setStyle("-fx-spacing: 5px;");
//        imageEditorVBox.setPadding(new Insets(0,0,0,0));
        imageEditorVBox.setSpacing(5);
        approverNameLabel.setPadding(new Insets(15,0,0,0));
    }

}
