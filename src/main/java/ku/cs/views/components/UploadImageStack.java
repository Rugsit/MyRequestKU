package ku.cs.views.components;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import ku.cs.services.ImageDatasource;

public class UploadImageStack extends StackPane {
    private StackPane stackPane;
    private String dir;
    private String fileName;
    private String curFileName;
    private VBox vBox;
    private HBox hBoxLine1;
    private HBox hBoxLine2;
    private DefaultLabel fileLabel;
    private DefaultButton uploadButton;
    private DefaultButton deleteButton;

    private ImageDatasource datasource;
    public UploadImageStack(String dir, String fileName, String curFileName) {
        this.stackPane = this;
        vBox = new VBox();
        hBoxLine1 = new HBox();
        hBoxLine2 = new HBox();

        fileLabel = new DefaultLabel("");
        uploadButton = new DefaultButton("#FF4E4E","#D03E3E","#FFFFFF");
        deleteButton = new DefaultButton("#FF4E4E","#D03E3E","#FFFFFF");

        this.dir = dir;
        this.fileName = fileName;
        this.curFileName = curFileName;
        inittialize();
        handleUploadButtonClick();
        handleDeleteButtonClick();
    }
    private void inittialize(){
        fileLabel.changeText(curFileName,18, FontWeight.NORMAL);
        uploadButton.changeBackgroundRadius(15);
        uploadButton.changeText("อัพโหลด",18, FontWeight.NORMAL);

        deleteButton.changeBackgroundRadius(15);
        deleteButton.changeText("ลบ",16, FontWeight.NORMAL);

        hBoxLine1.setAlignment(Pos.CENTER);
        hBoxLine1.setSpacing(10);
        hBoxLine1.getChildren().addAll(fileLabel, deleteButton);

        hBoxLine2.setAlignment(Pos.CENTER);
        hBoxLine2.getChildren().addAll(uploadButton);

        vBox.getChildren().addAll(hBoxLine1, hBoxLine2);
        stackPane.getChildren().addAll(vBox);

        datasource = new ImageDatasource(dir);
    }
    protected void handleUploadButtonClick(){
        uploadButton.setOnMouseClicked(e ->{
            curFileName = datasource.uploadImage(fileName);
            changeFileLabel(curFileName);
        });
    }
    protected void handleDeleteButtonClick(){
        deleteButton.setOnMouseClicked(e ->{
            if(curFileName != null && !curFileName.equalsIgnoreCase("no-image")){
                curFileName = "no-image";
                changeFileLabel(curFileName);
            }
        });
    }
    private void changeFileLabel(String text){
        fileLabel.changeText(text);
    }

    public String getCurFileName() {
        return curFileName;
    }
}
