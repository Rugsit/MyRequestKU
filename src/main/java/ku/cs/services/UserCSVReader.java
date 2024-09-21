package ku.cs.services;

import javafx.stage.FileChooser;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class UserCSVReader {
    public static ArrayList<String[]> read(int column) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose csv to read...");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("csv files", "*.csv"));

        File file = new File(System.getProperty("user.home") + File.separator + "Downloads");
        if (file.exists()) {
            fileChooser.setInitialDirectory(file);
        } else {
            fileChooser.setInitialDirectory(null);
        }

        ArrayList<String[]> data = new ArrayList<>();
        String[] dataLines;
        String line;
        File uploadedFile = fileChooser.showOpenDialog(null);
        if (uploadedFile == null) {
            return null;
        }
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(uploadedFile);
        } catch (FileNotFoundException e) {
            System.err.println("error reading file: " + uploadedFile.getAbsolutePath());
        }

        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

        try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)){
            while((line = bufferedReader.readLine()) != null) {
                dataLines = line.split(",");
                if(dataLines.length < column){
                    dataLines = Arrays.copyOf(dataLines, column);
                    for (int i = line.split(",").length; i < column; i++) {
                        dataLines[i] = "";
                    }
                }
                if(dataLines.length > column){
                    dataLines = Arrays.copyOf(dataLines, column);
                }
                data.add(dataLines);
            }

        } catch (IOException e) {
            System.err.println("error reading file: " + uploadedFile.getAbsolutePath());
        }
        return data;
    }
}