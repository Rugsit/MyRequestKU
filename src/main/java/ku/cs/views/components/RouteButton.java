package ku.cs.views.components;

import javafx.scene.control.Button;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class RouteButton extends DefaultButton {
    private String routeName;
    private Object data;

    //FOR NEW
    public RouteButton(String routeName,String baseColorHex, String hoverColorHex, String baseLabelColorHex) {
        super(baseColorHex, hoverColorHex, baseLabelColorHex);
        this.routeName = routeName;
        this.data = null;
    }
    public RouteButton(String routeName,String baseColorHex, String hoverColorHex, String baseLabelColorHex,Object data) {
        this(routeName,baseColorHex,hoverColorHex,baseLabelColorHex);
        this.data = data;
    }
    //FOR STATIC SCENEBUILDER
    public RouteButton(Button button, String routeName,String baseColorHex, String hoverColorHex, String baseLabelColorHex) {
        super(button, baseColorHex, hoverColorHex, baseLabelColorHex);
        this.routeName = routeName;
        this.data = null;
    }
    public RouteButton(Button button, String routeName,String baseColorHex, String hoverColorHex, String baseLabelColorHex,Object data){
        this(button,routeName,baseColorHex,hoverColorHex,baseLabelColorHex);
        this.data = data;
    }

    private void handleRoute(String routeName) {
        try {
            if(data != null) {
                FXRouter.goTo(routeName,data);
            }else{
                FXRouter.goTo(routeName);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void handleClickEvent() {
        button.setOnMouseClicked(e -> handleRoute(routeName));
    }
}