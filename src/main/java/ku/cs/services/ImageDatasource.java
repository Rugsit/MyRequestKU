package ku.cs.services;

import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class ImageDatasource{
    private final String imageDirectory;

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
        fileName = fileName.split("\\.")[0];
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
            return "";
        }

        // Get file extension of the uploaded files then append it to the file name
        String[] fileNameSplit = uploadedFile.getName().split("\\.");
        int fileExtensionIndex = fileNameSplit.length - 1;
        String fileExtension = fileNameSplit[fileExtensionIndex];

        File fileDestination = new File(imageDirectory + File.separator + fileName + "." + fileExtension);

        try {
            Files.copy(uploadedFile.toPath(), fileDestination.toPath(), REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println("Error uploading file");
        }

        return fileDestination.getName();
    }
}
