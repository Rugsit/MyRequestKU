package ku.cs.views.components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class CircleImage extends DefaultImage {
    public CircleImage(ImageView imageView) {
        super(imageView);
        makeIntoCircle(imageView);
    }

    public CircleImage(ImageView imageView, Image image) {
        super(imageView, image);
        makeIntoCircle(imageView);
    }

    private void makeIntoCircle(ImageView imageView){
        Rectangle rec = new Rectangle(imageView.getFitWidth(), imageView.getFitHeight());
        rec.setArcHeight(imageView.getImage().getHeight());
        rec.setArcWidth(imageView.getImage().getWidth());
        imageView.setClip(rec);
    }
}
