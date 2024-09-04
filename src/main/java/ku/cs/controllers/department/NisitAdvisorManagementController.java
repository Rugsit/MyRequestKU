package ku.cs.controllers.department;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ku.cs.views.components.*;

public class NisitAdvisorManagementController {
    @FXML private Label pageTitleLabel;

    @FXML private VBox allTableVBox;
    @FXML private VBox nisitTableVBox;
    @FXML private VBox advisorTableVBox;

    @FXML private HBox nisitTableHeaderHBox;
    @FXML private HBox advisorTableHeaderHBox;

    @FXML private Label nisitTableViewLabel;
    @FXML private TableView<?> nisitTableView;
    @FXML private Label advisorTableViewLabel;
    @FXML private TableView<?> advisorTableView;

    @FXML private VBox showNisitVBox;
    @FXML private VBox showAdvisorVBox;

    @FXML private ImageView nisitImageView;
    @FXML private Label nisitNamePrefixLabel;
    @FXML private Label nisitNameLabel;
    @FXML private Label nisitIdPrefixLabel;
    @FXML private Label nisitIdLabel;
    @FXML private Label nisitAdvisorNamePrefixLabel;
    @FXML private Label nisitAdvisorNameLabel;
    @FXML private Label nisitAdvisorIdPrefixLabel;
    @FXML private Label nisitAdvisorIdLabel;
    @FXML private HBox nisitNameHBox;

    @FXML private ImageView advisorImageView;
    @FXML private Label advisorNamePrefixLabel;
    @FXML private Label advisorNameLabel;
    @FXML private Label advisorIdPrefixLabel;
    @FXML private Label advisorIdLabel;
    @FXML private HBox advisorNameHBox;

    @FXML private Button changeAdvisorButton;
    @FXML private Button backButton;

    @FXML
    public void initialize(){
        initLabel();
        initButton();
        initShowVBox(showNisitVBox);
        initShowVBox(showAdvisorVBox);
        Image image = new Image(getClass().getResourceAsStream("/images/profile-test.png"));
        new SquareImage(nisitImageView,image).setClipImage(50,50);
        new SquareImage(advisorImageView,image).setClipImage(50,50);;
//        changeModelImageView(null, nisitImageView);
//        changeModelImageView(null, advisorImageView);
        showAdvisorVBox.setPadding(new Insets(20,0,0,0));
    }
    private void initLabel(){
        new DefaultLabel(pageTitleLabel);
        new DefaultLabel(nisitTableViewLabel);
        new DefaultLabel(advisorTableViewLabel);

        new DefaultLabel(nisitNamePrefixLabel);
        new DefaultLabel(nisitNameLabel);
        new DefaultLabel(nisitIdPrefixLabel);
        new DefaultLabel(nisitIdLabel);
        new DefaultLabel(nisitAdvisorNamePrefixLabel);
        new DefaultLabel(nisitAdvisorNameLabel);
        new DefaultLabel(nisitAdvisorIdPrefixLabel);
        new DefaultLabel(nisitAdvisorIdLabel);

        new DefaultLabel(advisorNamePrefixLabel);
        new DefaultLabel(advisorNameLabel);
        new DefaultLabel(advisorIdPrefixLabel);
        new DefaultLabel(advisorIdLabel);

    }
    private void initButton(){
        new RouteButton(backButton,"department-staff-request-list","transparent","#a6a6a6","#000000");
        new DefaultButton(changeAdvisorButton,"#FF4E4E","#D03E3E","#FFFFFF").changeBackgroundRadius(15);
    }
    private void initShowVBox(VBox vbox){
//        vbox.setSpacing(5);
        vbox.getChildren().forEach(child ->{
            if(child instanceof HBox){
                HBox hbox = (HBox) child;
                if(hbox.getId() != null && hbox.getId().contains("NameHBox")){
                    VBox.setMargin(hbox,new Insets(10,0,0,20));
                }else{
                    //Insets top right bottom left
                    VBox.setMargin(hbox,new Insets(5,0,0,20));
                }
                hbox.setSpacing(10);
            }
        });
    }
}
