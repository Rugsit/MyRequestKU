package ku.cs.views.components;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import ku.cs.services.ImageDatasource;
import ku.cs.services.department.Observer;
import ku.cs.services.department.Theme;

import java.util.HashMap;

public class UploadImageStack extends StackPane implements Observer<HashMap<String,String>> {
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
    private boolean deleteButtonClicked = false;
    private boolean uploadButtonClicked = false;
    private Theme theme = Theme.getInstance();

    private ImageDatasource datasource;
    public UploadImageStack(String dir, String fileName, String curFileName) {
        this.stackPane = this;
        vBox = new VBox();
        hBoxLine1 = new HBox();
        hBoxLine2 = new HBox();

        fileLabel = new DefaultLabel("");
        fileLabel.setMaxWidth(300);
        fileLabel.setWrapText(true);
        uploadButton = new DefaultButton("#FF4E4E","#D03E3E","#FFFFFF");
        deleteButton = new DefaultButton("#FF4E4E","#D03E3E","#FFFFFF");

        this.dir = dir;
        this.fileName = fileName;
        this.curFileName = curFileName;
        initialize();
        handleUploadButtonClick();
        handleDeleteButtonClick();
        theme.addObserver(this);
    }
    private void initialize(){
        fileLabel.changeText(curFileName,18, FontWeight.NORMAL);
        uploadButton.changeBackgroundRadius(15);
        uploadButton.changeText("อัปโหลด",18, FontWeight.NORMAL);

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
            uploadButtonClicked = true;
        });
    }
    protected void handleDeleteButtonClick(){
        deleteButton.setOnMouseClicked(e ->{
            if(curFileName != null && !curFileName.equalsIgnoreCase("no-image")){
                changeFileLabel("no-image");
                deleteButtonClicked = true;
            }
        });
    }
    private void changeFileLabel(String text){
        fileLabel.changeText(text);
    }

    public String getCurFileName() {
        return curFileName;
    }

    public void saveUploadedImage(){
        if(uploadButtonClicked && curFileName != null && !curFileName.equalsIgnoreCase("no-image")){
            curFileName = datasource.saveImage();
            changeFileLabel(curFileName);
        }
        uploadButtonClicked = false;
    }
    public void cancelUploadedImage(){
        if(uploadButtonClicked && curFileName != null && !curFileName.equalsIgnoreCase("no-image")){
            datasource.deleteFile("tmp");
            curFileName = "no-image";
            changeFileLabel(curFileName);
        }
        uploadButtonClicked = false;
    }
    public void performDeleteImage(){
        if(deleteButtonClicked && curFileName != null && !curFileName.equalsIgnoreCase("no-image")){
            datasource.deleteFile(curFileName);
            curFileName = "no-image";
            changeFileLabel(curFileName);
        }
        deleteButtonClicked = false;
    }
    public void cancelDeleteImage(){
        if(deleteButtonClicked && curFileName != null && !curFileName.equalsIgnoreCase("no-image")){
            changeFileLabel(curFileName);
        }
        deleteButtonClicked = false;
    }

    @Override
    public void update(HashMap<String, String> data) {
        fileLabel.changeLabelColor(data.get("textColor"));
    }
}
