package ku.cs.views.components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.shape.Rectangle;

public class CropImage extends DefaultImage{
    public CropImage(ImageView imageView){
        super(imageView);
    }
    public CropImage(ImageView imageView, Image image){
        super(imageView);
        setImage(image);
    }
    protected Image cropCenterImage(Image image){
        double width = image.getWidth();
        double height = image.getHeight();
        double viewHeight = imageView.getFitHeight();
        double viewWidth = imageView.getFitWidth();

        double cropWidth = (int)(width/ viewWidth) * viewWidth;
        double cropHeight = (int)(height/viewHeight) * viewHeight;
        if(viewWidth > width){
            cropWidth = width;
        }
        if(viewHeight > height){
            cropHeight = height;
        }
        double yOffset = (height - cropHeight) / 2;
        double xOffset = (width - cropWidth) / 2;
        //crop 1:1 keep aspect width:width at center height
        WritableImage cropped = new WritableImage((int)cropWidth, (int)cropHeight);
        PixelReader pixelReader = image.getPixelReader();
        cropped.getPixelWriter().setPixels(0, 0, (int)cropWidth, (int)cropHeight, pixelReader, (int)xOffset, (int)yOffset);
        return cropped;
    }
    public void setClipImage(double arcWidth,double arcHeight){
        Rectangle clip = new Rectangle(imageView.getFitWidth(),imageView.getFitHeight());
        clip.setArcWidth(arcWidth);
        clip.setArcHeight(arcHeight);
        imageView.setClip(clip);
    }
    @Override
    public void setImage(Image image){
        if(image != null){
            Image cropped = cropCenterImage(image);
            imageView.setImage(cropped);
        }
    }
}
