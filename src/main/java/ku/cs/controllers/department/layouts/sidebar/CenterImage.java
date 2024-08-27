package ku.cs.controllers.department.layouts.sidebar;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ku.cs.controllers.department.components.CropImage;

public class CenterImage {
    private VBox vBox;
    private ImageView imageView;
    public CenterImage(double width,double height,Image image) {
        initVBox(width,height);
        setVBoxStyle();
        double imgWidth = width*0.80;
        double imgHeight = height*0.80;
        initImageView(imgWidth,imgHeight,image);
        vBox.getChildren().add(imageView);
    }
    private void initVBox(double width,double height){
        vBox = new VBox();
        vBox.setPrefSize(width,height);
    }
    private void initImageView(double width,double height,Image image){
        imageView = new ImageView(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setImage(image);
//        new CropImage(imageView,image);
    }
    private void setVBoxStyle(){
        vBox.setStyle("-fx-alignment: CENTER;");
    }
    public void setMount(double x,double y){
        vBox.setLayoutX(x);
        vBox.setLayoutY(y);
    }
    public VBox getVBox() {
        return vBox;
    }
}
