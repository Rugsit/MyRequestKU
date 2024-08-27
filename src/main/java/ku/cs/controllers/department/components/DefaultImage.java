package ku.cs.controllers.department.components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class DefaultImage {

    protected ImageView imageView;
    public static final String fallbackImagePath = "/images/no-img.png";
    public static final Image fallbackImage;
    static {
        fallbackImage = new Image(DefaultImage.class.getResourceAsStream(fallbackImagePath));
    }
    public DefaultImage(ImageView imageView) {
        this.imageView = imageView;
        setImage(fallbackImage);
    }
    public DefaultImage(ImageView imageView,Image image) {
        this.imageView = imageView;
        setImage(image);
    }
    public void setImage(Image image) {
        if(image == null) {
            image = fallbackImage;
        }
        imageView.setImage(image);
    }
    public Image getImage(){
        return imageView.getImage();
    }
    public void removeImage(){
        setImage(null);
    }

    public void makeImageCircle() {
        Rectangle clip = new Rectangle(imageView.getFitWidth(),imageView.getFitHeight());
        clip.setArcWidth(imageView.getImage().getWidth());
        clip.setArcHeight(imageView.getImage().getHeight());
        imageView.setClip(clip);
    }

}
