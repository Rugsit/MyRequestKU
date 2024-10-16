package ku.cs.services;

import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ImageDatasource{
    private final String imageDirectory;
    private String fileName;

    public ImageDatasource(String imageDirectory){
        this.imageDirectory = "data"  + File.separator + "images" + File.separator + imageDirectory;
        checkIfDirectoryExisted();
    }

    private void checkIfDirectoryExisted(){
        File file;
        file = new File(imageDirectory);
        if(!file.exists()){
            file.mkdir();
        }
    }

    private String checkIfFileExisted(String fileName){
        String filePath = imageDirectory + File.separator + fileName;
        File file = new File(filePath);
        if (!file.exists()){
           return null;
        }
        return filePath;
    }

    public Image openImage(String fileName) {
        fileName = fileName + ".png";
        String filePath = checkIfFileExisted(fileName);
        if (filePath == null){
            System.err.println("Image not found");
            return null;
        }

        Image image = null;

        try{
            image = new Image("file:" + filePath);
        } catch (NullPointerException e){
            System.err.println("Error loading image");
        }

        return image;
    }

    public String uploadImage(String fileName) {
        this.fileName = fileName.split("\\.")[0];
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose image to upload...");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png"));

        File file = new File(System.getProperty("user.home") + File.separator + "Downloads");
        if (file.exists()) {
            fileChooser.setInitialDirectory(file);
        } else {
            fileChooser.setInitialDirectory(null);
        }

        File uploadedFile = fileChooser.showOpenDialog(null);
        if (uploadedFile == null) {
            return "no-image";
        }

        File fileDestination = new File(imageDirectory + File.separator + "tmp.png");
        try {
            BufferedImage bufferedImage = ImageIO.read(uploadedFile);
            ImageIO.write(bufferedImage, "png", fileDestination);
        } catch (IOException e) {
            System.err.println("Error uploading file");
        }

        return fileName;
    }

    public String saveImage() {
        BufferedImage bufferedImage;
        File tmpFile = new File(imageDirectory + File.separator + "tmp.png");
        if (!tmpFile.exists()) {
            return fileName;
        }
        File fileDestination = new File(imageDirectory + File.separator + fileName + ".png");
        try {
            bufferedImage = ImageIO.read(tmpFile);
            ImageIO.write(bufferedImage,"png", fileDestination);
            Files.delete(tmpFile.toPath());
        } catch (IOException e) {
            System.err.println("Error loading/saving image");
        }

        return fileName;
    }

    public void deleteFile(String fileName) {
        File tmp = new File(imageDirectory + File.separator + fileName + ".png");
        try {
            Files.delete(tmp.toPath());
        } catch (IOException e) {
            System.err.println("Error clearing temp file");
        }
    }
}
