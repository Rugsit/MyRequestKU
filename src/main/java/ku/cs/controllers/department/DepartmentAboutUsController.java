package ku.cs.controllers.department;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import ku.cs.controllers.AboutUsController;
import ku.cs.models.Session;
import ku.cs.services.FXRouter;
import ku.cs.services.Observer;
import ku.cs.services.Theme;
import ku.cs.views.components.CircleImage;
import ku.cs.views.components.DefaultLabel;
import ku.cs.views.layouts.sidebar.SidebarController;

import java.io.IOException;
import java.util.HashMap;

public class DepartmentAboutUsController implements Observer<HashMap<String, String>> {
    @FXML private AnchorPane mainAnchorPane;
    @FXML private StackPane mainStackPane;//FOR MANUAL PDF VIEW
    @FXML private Label pageTitleLabel;
    private Session session;
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
        sidebarController = new SidebarController("aboutus",session);
        mainAnchorPane.getChildren().add(sidebarController.getVBox());
        loadAboutUs();

        theme.addObserver(this);
        theme.notifyObservers(theme.getTheme());

    }
    private void initLabel() {
        new DefaultLabel(pageTitleLabel);
    }
    private void initChildren(ObservableList<Node> children) {
        for(Node child : children){
            if(child instanceof Label){
                Label label = (Label) child;
                new DefaultLabel(label);
            }
            if(child instanceof ImageView){
                ImageView imageView = (ImageView) child;
                new CircleImage(imageView,imageView.getImage());
            }
            if(child instanceof VBox){
                initChildren(((VBox) child).getChildren());
            }
        }
    }
    private void loadAboutUs() {
        try {
            String viewPath = "/ku/cs/views/about-us-pane.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            AboutUsController controller = fxmlLoader.getController();
            controller.initialize();
            initChildren(pane.getChildren());
            pane.setPrefSize(1020,720);
            pane.setLayoutX(260);
            pane.setLayoutY(50);
            mainAnchorPane.getChildren().add(pane);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void update(HashMap<String, String> data) {
        mainAnchorPane.setStyle(mainAnchorPane.getStyle()+"-fx-background-color: " + data.get("secondary") + ";");
    }
}
