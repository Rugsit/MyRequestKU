package ku.cs.controllers.department;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import ku.cs.models.Session;
import ku.cs.models.user.User;
import ku.cs.services.FXRouter;
import ku.cs.views.components.DefaultLabel;
import ku.cs.views.layouts.sidebar.SidebarController;

public class ProfileController {
    @FXML private AnchorPane mainAnchorPane;
    @FXML private StackPane mainStackPane;
    @FXML private Label pageTitleLabel;
    private Session session;
private void initRouteData(){
    Object object = FXRouter.getData();
    if(object instanceof Session){
        this.session = (Session) object;
    }else{
        session = null;
    }
}
    @FXML
    public void initialize() {
        initRouteData();
        initLabel();
        mainAnchorPane.getChildren().add(new SidebarController("profile",session).getVBox());

    }
    private void initLabel() {
        new DefaultLabel(pageTitleLabel);
    }


}
