package ku.cs.services;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileTime;
import java.time.Instant;

public class PDFDatasource {
        private final String fileDirectory;
        private String fileName;

        public PDFDatasource(){
            this.fileDirectory = "data"  + File.separator + "requests" + File.separator + "signatures";
            checkIfDirectoryExisted();
        }

        private void checkIfDirectoryExisted(){
            File file;
            file = new File(fileDirectory);
            if(!file.exists()){
                file.mkdir();
            }
        }

        public String uploadFile(String fileName) {
            this.fileName = fileName.split("\\.")[0];
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose PDF to upload...");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

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

            File fileDestination = new File(fileDirectory + File.separator + "tmp.pdf");
            try {
                Files.copy(uploadedFile.toPath(), fileDestination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                System.err.println("Error uploading file");
            }

            return fileName;
        }

        public String downloadFile(String fileName) {
            this.fileName = this.fileName = fileName.split("\\.")[0];
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Choose directory to download file to...");
            File file = new File(System.getProperty("user.home") + File.separator + "Downloads");
            if (file.exists()) {
                directoryChooser.setInitialDirectory(file);
            } else {
                directoryChooser.setInitialDirectory(null);
            }

            File destinationFile = directoryChooser.showDialog(null);
            if (destinationFile == null) {
                return null;
            }

            destinationFile = new File(destinationFile.getPath() + File.separator + fileName + ".pdf");
            File fileDirectory = new File(this.fileDirectory + File.separator + fileName + ".pdf");
            try {
                Files.copy(fileDirectory.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                Files.setLastModifiedTime(destinationFile.toPath(), FileTime.from(Instant.now()));
            } catch (IOException e) {
                System.err.println("Error downloading file");
            }
            return fileName;
        }

        public String saveFile() {
            File tmpFile = new File(fileDirectory + File.separator + "tmp.pdf");
            if (!tmpFile.exists()) {
                return fileName;
            }
            File fileDestination = new File(fileDirectory + File.separator + fileName + ".pdf");
            try {
                Files.copy(tmpFile.toPath(), fileDestination.toPath(), StandardCopyOption.REPLACE_EXISTING);
                Files.setLastModifiedTime(fileDestination.toPath(), FileTime.from(Instant.now()));
                deleteFile("tmp");
            } catch (IOException e) {
                System.err.println("Error loading/saving file");
            }

            return fileName;
        }

        public void deleteFile(String fileName) {
            File tmp = new File(fileDirectory + File.separator + fileName + ".pdf");
            try {
                Files.delete(tmp.toPath());
            } catch (IOException e) {
                System.err.println("Error clearing temp file");
            }
        }
        private String checkIfFileExisted(String fileName){
            String filePath = fileDirectory + File.separator + fileName;
            File file = new File(filePath);
            if (!file.exists()){
                return null;
            }
            return filePath;
        }
        public File getPDFFile(String fileName) {
            fileName = fileName + ".pdf";
            String filePath = checkIfFileExisted(fileName);
            if (filePath == null){
                System.err.println("PDF not found");
                return null;
            }

            File pdfFile = null;

            try{
                pdfFile = new File(filePath);
            } catch (Exception e){
                System.err.println("Error loading PDF");
                e.printStackTrace();
            }

            return pdfFile;
        }

}
