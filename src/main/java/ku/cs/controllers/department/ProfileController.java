package ku.cs.controllers.department;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import ku.cs.models.Session;
import ku.cs.models.user.User;
import ku.cs.services.FXRouter;
import ku.cs.services.Observer;
import ku.cs.services.Theme;
import ku.cs.views.components.DefaultLabel;
import ku.cs.views.layouts.sidebar.SidebarController;

import java.util.HashMap;

public class ProfileController implements Observer<HashMap<String, String>> {
    @FXML private AnchorPane mainAnchorPane;
    @FXML private StackPane mainStackPane;
    @FXML private Label pageTitleLabel;
    private Session session;
    private Theme theme = Theme.getInstance();
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
        theme.clearObservers();
        initRouteData();
        initLabel();
        mainAnchorPane.getChildren().add(new SidebarController("profile",session).getVBox());
        theme.addObserver(this);
        theme.notifyObservers(theme.getTheme());

    }
    private void initLabel() {
        new DefaultLabel(pageTitleLabel);
    }


    @Override
    public void update(HashMap<String, String> data) {
        mainAnchorPane.setStyle(mainAnchorPane.getStyle()+"-fx-background-color: " + data.get("secondary") + ";");
    }
}
