package ku.cs.controllers.department.components;

import javafx.scene.control.Button;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class RouteButton extends DefaultButton {
    private String routeName;

    public RouteButton(String routeName,String baseColorHex, String hoverColorHex, String baseLabelColorHex) {
        super(baseColorHex, hoverColorHex, baseLabelColorHex);
        this.routeName = routeName;
    }
    public RouteButton(Button button, String routeName,String baseColorHex, String hoverColorHex, String baseLabelColorHex) {
        super(button, baseColorHex, hoverColorHex, baseLabelColorHex);
        this.routeName = routeName;
    }

    private void handleRoute(String routeName) {
        try {
            FXRouter.goTo(routeName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void handleClickEvent() {
        button.setOnMouseClicked(e -> handleRoute(routeName));
    }
}