package ku.cs.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
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

    private Stage stage;
    private String lightCSS;
    private String darkCSS;

    @FXML
    private void initialize() {
        Platform.runLater(this::changeCssPage);
        fontSizeChoiceBox.getItems().addAll("จิ๋ว", "เล็ก", "กลาง", "ใหญ่");
        if (Theme.getInstance().getCurrentFont().contains("large")) {
            fontSizeChoiceBox.setValue("ใหญ่");
        } else if (Theme.getInstance().getCurrentFont().contains("medium")) {
            fontSizeChoiceBox.setValue("กลาง");
        } else if (Theme.getInstance().getCurrentFont().contains("small")) {
            fontSizeChoiceBox.setValue("เล็ก");
        } else if (Theme.getInstance().getCurrentFont().contains("tiny")) {
            fontSizeChoiceBox.setValue("จิ๋ว");
        }

        fontChoiceBox.getItems().addAll("Maehongson", "THSarabunNew", "PrintAble4u");
        if (Theme.getInstance().getCurrentFontFamily().contains("Maehongson")) {
            fontChoiceBox.setValue("Maehongson");
        } else if (Theme.getInstance().getCurrentFontFamily().contains("THSarabunNew")) {
            fontChoiceBox.setValue("THSarabunNew");
        } else if (Theme.getInstance().getCurrentFontFamily().contains("printAble4u")) {
            fontChoiceBox.setValue("PrintAble4u");
        }

        fontChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String pathToFile = "";
                if (newValue.equals("Maehongson")) {
                    pathToFile = "PK-Maehongson-Demo.css";
                } else if (newValue.equals("THSarabunNew")) {
                    pathToFile = "THSarabunNew.css";
                } else if (newValue.equals("PrintAble4u")) {
                    pathToFile = "printAble4u-font.css";
                }
                Theme.getInstance().setCurrentFontFamily(pathToFile);
                changeCssPage();
            }
        });


        fontSizeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String pathToFile = "";
                if (newValue.equals("จิ๋ว")) {
                    pathToFile = "font-tiny.css";
                } else if (newValue.equals("เล็ก")) {
                    pathToFile = "font-small.css";
                } else if (newValue.equals("กลาง")) {
                    pathToFile = "font-medium.css";
                } else if (newValue.equals("ใหญ่")) {
                    pathToFile = "font-large.css";
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
    private void setThemeDark() {
        Theme.getInstance().setCurrentTheme("dark");
        Theme.getInstance().setTheme("dark");
        changeCssPage();
    }

    @FXML
    private void setThemeLight() {
        Theme.getInstance().setCurrentTheme("light");
        Theme.getInstance().setTheme("light");
        changeCssPage();
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
