package ku.cs.views.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import ku.cs.services.Observer;
import ku.cs.services.Theme;

import java.util.HashMap;

public class ConfirmStack extends StackPane implements Observer<HashMap<String,String>> {
    private DefaultLabel titleLabel;
    private DefaultLabel descriptionLabel;
    private String title;
    private String description;

    private StackPane stackPane;
    private Pane backgroundPane;
    private VBox mainBox;
    private VBox line1VBox;
    private VBox line2VBox;
    private VBox line3VBox;
    private HBox line1;
    private HBox line2;
    private HBox line3;
    private DefaultButton acceptButton;
    private DefaultButton declineButton;
    private Theme theme = Theme.getInstance();
    public ConfirmStack(String title, String description) {
        this.title = title;
        this.description = description;
        titleLabel = new DefaultLabel("");
        descriptionLabel = new DefaultLabel("");
        line1 = new HBox();
        line2 = new HBox();
        line3 = new HBox();
        initButton();
        initLineHBox();

        stackPane = this;
        backgroundPane = new Pane();
        mainBox = new VBox();


        initialize();
        update(theme.getTheme());
    }
    protected void initialize(){
        initStackPain();
        initBackgroundPane();
        initMainBox();
        stackPane.getChildren().addAll(backgroundPane,mainBox);

    }
    private void initStackPain(){
        stackPane.setLayoutX(0);
        stackPane.setLayoutY(0);
        stackPane.setPrefSize(1280, 720);
        stackPane.setStyle("-fx-background-color: transparent;");
        stackPane.setAlignment(Pos.CENTER);
    }
    private void initBackgroundPane() {
        backgroundPane.setLayoutX(0);
        backgroundPane.setLayoutY(0);
        backgroundPane.setPrefSize(1280, 720);
        backgroundPane.setStyle("-fx-background-color: black");
        backgroundPane.setOpacity(0.3);
    }
    private void initMainBox(){
        double mainWidth = 500;
        double mainHeight = 300;
        mainBox.setMaxWidth(mainWidth);
        mainBox.setMaxHeight(mainHeight);
        mainBox.setStyle("-fx-background-radius: 50;-fx-background-color: white;");

        line1VBox = new VBox(line1);
        line2VBox = new VBox(line2);
        line3VBox = new VBox(line3);

        line1VBox.setMaxWidth(mainWidth);
        line1VBox.setPrefHeight(75);
        line1VBox.setAlignment(Pos.CENTER);
        line2VBox.setPrefWidth(mainWidth);
        line2VBox.setPrefHeight(125);
        line2VBox.setAlignment(Pos.TOP_LEFT);
        line3VBox.setPrefWidth(mainWidth);
        line3VBox.setPrefHeight(100);
        line3VBox.setAlignment(Pos.CENTER);

        VBox.setMargin(line2, new Insets(0, 50, 0, 50));
        mainBox.getChildren().addAll(line1VBox,line2VBox,line3VBox);
    }
    private void initButton(){
        acceptButton = new DefaultButton("#78D88C","#5AA469","white");
        acceptButton.setButtonSize(215,60);
        acceptButton.changeBackgroundRadius(25);
        acceptButton.changeText("ยืนยัน",28,FontWeight.NORMAL);
        declineButton = new DefaultButton("#FF8080","#BC5F5F","white");
        declineButton.setButtonSize(215,60);
        declineButton.changeBackgroundRadius(25);
        declineButton.changeText("ยกเลิก",28,FontWeight.NORMAL);
        handleAcceptButton();
        handleDeclineButton();
    }
    private void initLineHBox(){
        titleLabel.changeText(title,32, FontWeight.BOLD);
        descriptionLabel.changeText(description,24, FontWeight.NORMAL);
        descriptionLabel.setWrapText(true);

        line1.setAlignment(Pos.CENTER);
        line2.setAlignment(Pos.CENTER_LEFT);
        line3.setAlignment(Pos.CENTER);

        line1.getChildren().add(titleLabel);
        line2.getChildren().add(descriptionLabel);
        line3.getChildren().addAll(acceptButton,declineButton);
        line3.setSpacing(20);

    }
    public DefaultButton getAcceptButton(){
        return acceptButton;
    }
    public DefaultButton getDeclineButton(){
        return declineButton;
    }
    protected void handleAcceptButton(){
        acceptButton.setOnMouseClicked(e -> {
        });
    }
    protected void handleDeclineButton(){
        declineButton.setOnMouseClicked(e ->{
        });
    }

    @Override
    public void update(HashMap<String, String> data) {
        mainBox.setStyle(mainBox.getStyle()+"-fx-background-color: "+ data.get("secondary") +";");

        titleLabel.update(data);
        descriptionLabel.update(data);
        acceptButton.update(data);
        declineButton.update(data);
    }
}
