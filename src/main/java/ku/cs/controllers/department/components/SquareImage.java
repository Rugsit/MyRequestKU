package ku.cs.controllers.department.components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

public class SquareImage extends CropImage{
    public SquareImage(ImageView imageView){
        super(imageView);
    }
    public SquareImage(ImageView imageView, Image image){
        super(imageView);
        setImage(image);
    }
    @Override
    protected Image cropCenterImage(Image image){
        double width = image.getWidth();
        double height = image.getHeight();
//        double viewHeight = imageView.getFitHeight();
//        double viewWidth = imageView.getFitWidth();

        double cropWidth = width;
        double cropHeight = width;

        if(width > height){
            cropHeight = height;
        }
        double yOffset = (height - cropHeight) / 2;
//        double xOffset = (width - cropWidth) / 2;
        //crop 1:1 keep aspect width:width at center height
        WritableImage cropped = new WritableImage((int)cropWidth, (int)cropHeight);
        PixelReader pixelReader = image.getPixelReader();
        cropped.getPixelWriter().setPixels(0, 0, (int)cropWidth, (int)cropHeight, pixelReader, 0, (int)yOffset);
        return cropped;
    }
}
