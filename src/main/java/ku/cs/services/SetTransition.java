package ku.cs.services;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class SetTransition {
    public static SetTransition setTransition;
    private Timeline timeline;

    public static final SetTransition getInstance() {
        if (setTransition == null) {
            setTransition = new SetTransition();
        }
        return setTransition;
    }
    public static void setButtonBounce(Button button) {
        button.setOnMousePressed(event -> {
            ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), button);
            scaleUp.setToX(1.1);  // ขยายปุ่มตามแนวนอน 1.1 เท่า
            scaleUp.setToY(1.1);  // ขยายปุ่มตามแนวตั้ง 1.1 เท่า
            scaleUp.play();       // เล่น animation เมื่อกดปุ่ม
        });

        // ตั้งค่าให้กลับเป็นขนาดเดิมเมื่อปล่อยปุ่ม
        button.setOnMouseReleased(event -> {
            ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), button);
            scaleDown.setToX(1.0); // กลับมาขนาดเดิมตามแนวนอน
            scaleDown.setToY(1.0); // กลับมาขนาดเดิมตามแนวตั้ง
            scaleDown.play();      // เล่น animation เมื่อปล่อยปุ่ม
        });
    }

    public void setSlideImageShow(ImageView imageView, String[] imagePaths) {
        final int[] currentImageIndex = {0};
        // Create a Timeline to switch images
        if (timeline != null) {
            return;
        }
        timeline = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), imageView);
            fadeOut.setFromValue(1.0);  // Fade in from 0% opacity
            fadeOut.setToValue(0.0);    // Fade to 100% opacity
            fadeOut.setOnFinished(e -> {
                currentImageIndex[0] = (currentImageIndex[0] + 1) % imagePaths.length;
                javafx.scene.image.Image nextImage = new Image(imagePaths[currentImageIndex[0]]);
                imageView.setImage(nextImage);
                FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), imageView);
                fadeIn.setFromValue(0.0); // Fade in
                fadeIn.setToValue(1.0);
                fadeIn.play(); // Play fade-in after the image changes
            });
            fadeOut.play(); // Start fade-out
            System.out.println("test");
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play(); // Start the slideshow
    }

    public Timeline getTimeline() {
        return timeline;
    }

    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
    }
}
