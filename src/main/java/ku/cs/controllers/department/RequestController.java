package ku.cs.controllers.department;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ku.cs.views.components.DefaultButton;
import ku.cs.views.components.DefaultLabel;
import ku.cs.views.components.RouteButton;
import ku.cs.views.components.SquareImage;
import ku.cs.models.Request;
import ku.cs.services.FXRouter;

import java.time.format.DateTimeFormatter;

public class RequestController {
    @FXML private Label pageTitleLabel;

    @FXML private ImageView requestProfileImageView;
    @FXML private Label requestTypeLabel;
    @FXML private Label requestNisitIdPrefixLabel;
    @FXML private Label requestNisitIdLabel;
    @FXML private Label requestNisitNamePrefixLabel;
    @FXML private Label requestNisitNameLabel;
    @FXML private Label requesTimestampPrefixLabel;
    @FXML private Label requesTimestampLabel;
    @FXML private Label requestIdPrefixLabel;
    @FXML private Label requestIdLabel;

    @FXML private ImageView requestStatusImageView;
    @FXML private Label requestStatusLabel;

    @FXML private Button addApproverButton;
    @FXML private Button requestInfoButton;
    @FXML private Button approveButton;
    @FXML private Button rejectButton;
    @FXML private Button backButton;

    @FXML private TableView approverTableView;
    @FXML private ImageView editorStatusCheckGreyImageView;
    @FXML private Label editorStatusImageLabel;
    @FXML
    public void initialize(){
        initLabel();
        initButton();
        Image image = new Image(getClass().getResourceAsStream("/images/profile-test.png"));
        new SquareImage(requestProfileImageView,image).setClipImage(50,50);
    }
    private void initLabel(){
        Request req = (Request) FXRouter.getData();
        DefaultLabel reqType,reqName,reqNisitId,reqTimestamp;
        new DefaultLabel(pageTitleLabel);

        reqType = new DefaultLabel(requestTypeLabel);
        new DefaultLabel(requestNisitIdPrefixLabel);

        new DefaultLabel(requestNisitIdPrefixLabel);
        reqNisitId = new DefaultLabel(requestNisitIdLabel);

        new DefaultLabel(requestNisitNamePrefixLabel);
        reqName = new DefaultLabel(requestNisitNameLabel);

        new DefaultLabel(requesTimestampPrefixLabel);
        reqTimestamp = new DefaultLabel(requesTimestampLabel);

        new DefaultLabel(requestIdPrefixLabel);
        new DefaultLabel(requestIdLabel);

        new DefaultLabel(requestStatusLabel);
        new DefaultLabel(editorStatusImageLabel);

        if(req!=null){
            reqType.setText(req.getRequestType());
            reqName.setText(req.getName());
            reqNisitId.setText(req.getNisitId());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            reqTimestamp.setText(req.getTimeStamp().format(formatter));
        }
    }
    private void initButton(){
        new RouteButton(backButton,"department-staff-request-list","transparent","#a6a6a6","#000000");
        new DefaultButton(addApproverButton,"#7FE8FF","#a6a6a6","#000000").changeBackgroundRadius(15);
        new DefaultButton(requestInfoButton,"#FFA1D9","#a6a6a6","#000000").changeBackgroundRadius(15);
        new DefaultButton(approveButton,"transparent","#a6a6a6","#00DE59");
        new DefaultButton(rejectButton,"transparent","#a6a6a6","#FF5D5D");
    }







}
