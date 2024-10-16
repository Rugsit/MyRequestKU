package ku.cs.controllers.requests;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ku.cs.services.PathGenerator;
import ku.cs.services.Theme;

public class ErrorGeneralRequestFormController {
    private Stage stage;

    @FXML
    private Label errorMessage;
    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    public void onAgreeClick() {
        stage.close();
    }

    @FXML
    private void initialize() {
        updateStyle();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage.setText(errorMessage);
    }

    public void updateStyle() {
        Theme.getInstance().loadCssToPage(mainAnchorPane, new PathGenerator() {
            @Override
            public String getThemeDarkPath() {
                return getClass().getResource("/ku/cs/styles/general-dark.css").toString();
            }
            @Override
            public String getThemeLightPath() {
                return getClass().getResource("/ku/cs/styles/general.css").toString();
            }
        });
    }
}
