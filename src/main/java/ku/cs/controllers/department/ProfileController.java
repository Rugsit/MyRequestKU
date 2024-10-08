package ku.cs.controllers.department;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import ku.cs.controllers.ParentController;
import ku.cs.controllers.UserProfileCardController;
import ku.cs.models.Session;
import ku.cs.models.user.DepartmentUser;
import ku.cs.models.user.User;
import ku.cs.services.FXRouter;
import ku.cs.services.Observer;
import ku.cs.services.Theme;
import ku.cs.views.components.DefaultLabel;
import ku.cs.views.layouts.sidebar.SidebarController;

import java.io.IOException;
import java.util.HashMap;

public class ProfileController implements Observer<HashMap<String, String>>, ParentController {
    @FXML private AnchorPane mainAnchorPane;
    @FXML private StackPane mainStackPane;
    @FXML private Label pageTitleLabel;
    private Session session;
    private DepartmentUser loginUser;
    private SidebarController sidebarController;
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
        setLoginUser(session.getUser());
        sidebarController = new SidebarController("profile",session);
        mainAnchorPane.getChildren().add(sidebarController.getVBox());
        theme.addObserver(this);

        loadProfileCard();
    }
    private void initLabel() {
        new DefaultLabel(pageTitleLabel);
    }

    @Override
    public void setLoginUser(User loginUser) {
        this.loginUser = (DepartmentUser) loginUser;
    }

    @Override
    public void loadProfile() {
        mainAnchorPane.getChildren().removeLast();
        mainAnchorPane.getChildren().removeLast();

        theme.removeObserver(sidebarController);
        sidebarController = new SidebarController("profile",session);
        mainAnchorPane.getChildren().add(sidebarController.getVBox());

        loadProfileCard();
    }

    private void loadProfileCard() {
        try {
            String viewPath = "/ku/cs/views/user-profile-card.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            UserProfileCardController controller = fxmlLoader.getController();
            controller.setLoginUser(loginUser);
            controller.setParentController(this);
            controller.initialize();
            pane.setLayoutX(432);
            pane.setLayoutY(157);
            mainAnchorPane.getChildren().add(pane);

            theme.notifyObservers(theme.getTheme());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(HashMap<String, String> data) {
        mainAnchorPane.setStyle(mainAnchorPane.getStyle()+"-fx-background-color: " + data.get("secondary") + ";");
    }
}
