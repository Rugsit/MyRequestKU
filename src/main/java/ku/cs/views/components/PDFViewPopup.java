package ku.cs.views.components;

import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PDFViewPopup extends BlankPopupStack{
    protected File pdfFile;
    protected DefaultLabel titleLabel;
    protected VBox mainBox;
    protected ScrollPane scrollPane;
    public PDFViewPopup(File pdfFIle){
        this.pdfFile = pdfFIle;
        initPDFPages();
    }
    @Override
    protected void initPopupView(){
        mainBox = new VBox();
        mainBox.setMaxWidth(1000);
        mainBox.setMaxHeight(600);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setSpacing(10);
        mainBox.setStyle("-fx-background-color: white;-fx-background-radius: 50");
        //TITLE
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);
        titleLabel = new DefaultLabel("");
        titleLabel.changeText("PDFView",32,FontWeight.BOLD);
        container.getChildren().add(titleLabel);
        mainBox.getChildren().add(container);
        VBox.setMargin(container,new Insets(10,0,0,0));

        //PDF ScrollPane
        scrollPane = new ScrollPane();
        scrollPane.setMaxWidth(900);
        scrollPane.setMaxHeight(450);
        scrollPane.setPrefWidth(900);
        scrollPane.setPrefHeight(450);
        scrollPane.setFitToWidth(true);

        //LINE END BUTTON
        HBox lineEnd = new HBox(secondButton,firstButton);
        lineEnd.setSpacing(20);
        lineEnd.setPrefHeight(100);
        lineEnd.setAlignment(Pos.CENTER);
        mainBox.getChildren().addAll(scrollPane,lineEnd);
        stackPane.getChildren().add(mainBox);
    }
    protected void initPDFPages(){
        Task<VBox> loadTask = new Task<>() {
            @Override
            protected VBox call() {
                return getConvertedPDF(pdfFile,800);
            }
        };

        loadTask.setOnSucceeded(event -> {
            scrollPane.setContent(loadTask.getValue());
        });

        firstButton.changeText("ดาวน์โหลด PDF");
        secondButton.changeText("ปิด");
        new Thread(loadTask).start();
    }
    private VBox getConvertedPDF(File file,double width){
        PDDocument document;
        PDFRenderer pdfRenderer;
        VBox pdfPages = new VBox();
        pdfPages.setSpacing(10);
        pdfPages.setAlignment(Pos.CENTER);
        try {
            document = PDDocument.load(file);
            pdfRenderer = new PDFRenderer(document);
            //CONVERT ALL PAGES TO IMAGE
            for (int page = 0; page < document.getNumberOfPages(); page++) {
                BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(page, 150);
                Image pdfImage = convertBufferedImageToImage(bufferedImage);

                ImageView imageView = new ImageView(pdfImage);
                imageView.setPreserveRatio(true);
                imageView.setFitWidth(width);

                pdfPages.getChildren().add(imageView);
            }
            document.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return pdfPages;
    }
    private Image convertBufferedImageToImage(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        WritableImage writableImage = new WritableImage(width, height);
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        //Extract pixels from the BufferedImage
        int[] buffer = new int[width * height];
        bufferedImage.getRGB(0, 0, width, height, buffer, 0, width);

        //Write pixels to the WritableImage
        pixelWriter.setPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), buffer, 0, width);

        return writableImage;
    }

}
