package ku.cs.services;

import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.ReadOnlyBufferException;
import java.nio.file.AccessDeniedException;
import java.nio.file.CopyOption;
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

    private void checkIfFileExisted(String fileName){
        String filePath = imageDirectory + File.separator + fileName;
        File file = new File(filePath);
        if(!file.exists()){
            try{
                file.createNewFile();
            } catch (IOException e){
                System.out.println("Error creating file");
            }
        }
    }

    //TODO: finish method to open image from datasource and send back to caller
    public Image openImage(String fileName) {
        checkIfFileExisted(fileName);
        return null;
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

        String[] fileNameSplit = uploadedFile.getName().split("\\.");
        int fileExtensionIndex = fileNameSplit.length - 1;
        String fileExtension = fileNameSplit[fileExtensionIndex];

        File fileDestination = new File(imageDirectory + File.separator + fileName + "." + fileExtension);

        try {
            Files.copy(uploadedFile.toPath(), fileDestination.toPath(), REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("Error uploading file");
        }

        return fileDestination.getName();
    }
}
