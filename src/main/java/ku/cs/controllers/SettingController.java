package ku.cs.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ku.cs.services.PathGenerator;
import ku.cs.services.Theme;

public class SettingController {
    @FXML
    private ChoiceBox<String> fontSizeChoiceBox;
    @FXML
    private ChoiceBox<String> fontChoiceBox;
    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button changeThemeButton;

    private Stage stage;
    private String lightCSS;
    private String darkCSS;

    @FXML
    private void initialize() {
        Platform.runLater(this::changeCssPage);
        Platform.runLater(this::updateChangeThemeButton);
        fontSizeChoiceBox.getItems().addAll( "เล็ก", "กลาง", "ใหญ่");
        if (Theme.getInstance().getCurrentFont().contains("large")) {
            fontSizeChoiceBox.setValue("ใหญ่");
        } else if (Theme.getInstance().getCurrentFont().contains("medium")) {
            fontSizeChoiceBox.setValue("กลาง");
        } else if (Theme.getInstance().getCurrentFont().contains("small")) {
            fontSizeChoiceBox.setValue("เล็ก");
        }

        fontChoiceBox.getItems().addAll( "PrintAble4u", "Krub");
        if (Theme.getInstance().getCurrentFontFamily().contains("Krub")) {
            fontChoiceBox.setValue("Krub");
        } else if (Theme.getInstance().getCurrentFontFamily().contains("printAble4u")) {
            fontChoiceBox.setValue("PrintAble4u");
        }

        fontChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String pathToFile = "";
                if (newValue.equals("Krub")) {
                    pathToFile = "Krub.css";
                    Theme.getInstance().setTextFont("Krub");
                } else if (newValue.equals("PrintAble4u")) {
                    pathToFile = "printAble4u-font.css";
                    Theme.getInstance().setTextFont("PrintAble4U");
                }
                Theme.getInstance().setCurrentFontFamily(pathToFile);
                changeCssPage();
            }
        });


        fontSizeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String pathToFile = "";
                if (newValue.equals("เล็ก")) {
                    pathToFile = "font-small.css";
                    Theme.getInstance().setTextSize("small");
                } else if (newValue.equals("กลาง")) {
                    pathToFile = "font-medium.css";
                    Theme.getInstance().setTextSize("normal");
                } else if (newValue.equals("ใหญ่")) {
                    pathToFile = "font-large.css";
                    Theme.getInstance().setTextSize("large");
                }
                Theme.getInstance().setCurrentFont(pathToFile);
                changeCssPage();
            }
        });

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMainCSS(String dark, String light) {
        this.darkCSS = dark;
        this.lightCSS = light;
    }

    public void setMainAnchorPane(AnchorPane mainAnchorPane) {
        this.mainAnchorPane = mainAnchorPane;
    }

    @FXML
    private void changeTheme() {
        if (Theme.getInstance().getCurrentTheme().equals("dark")) {
            Theme.getInstance().setCurrentTheme("light");
            Theme.getInstance().setTheme("light");
            changeCssPage();
            updateChangeThemeButton();

        } else {
            Theme.getInstance().setCurrentTheme("dark");
            Theme.getInstance().setTheme("dark");
            changeCssPage();
            updateChangeThemeButton();
        }
    }

    private void updateChangeThemeButton() {
        if (Theme.getInstance().getCurrentTheme().equals("dark")) {
            changeCssPage();
            changeThemeButton.setStyle("-fx-background-radius: 150;\n" +
                    "    -fx-cursor: hand;\n" +
                    "    -fx-background-color: #2731B7;");
            ImageView imageView = new ImageView(new Image(getClass().getResource("/images/icons/moon-icon.png").toString()));
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            changeThemeButton.setGraphic(imageView);

        } else {
            changeCssPage();
            changeThemeButton.setStyle("-fx-background-radius: 150;\n" +
                    "    -fx-cursor: hand;\n" +
                    "    -fx-background-color: #69eeff;");
            ImageView imageView = new ImageView(new Image(getClass().getResource("/images/icons/sun-icon.png").toString()));
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            changeThemeButton.setGraphic(imageView);
        }
    }

    private void changeCssPage(){
        Theme.getInstance().loadCssToPage(mainAnchorPane, new PathGenerator() {
            @Override
            public String getThemeDarkPath() {
                return darkCSS;
            }
            @Override
            public String getThemeLightPath() {
                return lightCSS;
            }
        });

        Theme.getInstance().loadCssToPage(anchorPane, new PathGenerator() {
            @Override
            public String getThemeDarkPath() {
                return darkCSS;
            }
            @Override
            public String getThemeLightPath() {
                return lightCSS;
            }
        });
    }
    @FXML
    private void onExitClick() {
        stage.close();
    }
}
