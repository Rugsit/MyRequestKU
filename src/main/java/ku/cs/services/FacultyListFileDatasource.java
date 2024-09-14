package ku.cs.services;

import ku.cs.models.faculty.Faculty;
import ku.cs.models.faculty.FacultyList;

import java.io.*;

public class FacultyListFileDatasource implements Datasource<FacultyList> {
    private final String directoryName;
    private String fileName;

    public FacultyListFileDatasource(String directoryName) {
        this.directoryName = directoryName;
        this.fileName = "faculty.csv";
        checkIfFileExist();
    }

    private void checkIfFileExist() {
        String filePath = directoryName;
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }

        filePath = filePath + File.separator + fileName;
        file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("error creating file: " + filePath);
            }
        }
    }

    @Override
    public FacultyList readData() {
        FacultyList facultyList = new FacultyList();
        String filePath = directoryName + File.separator + fileName;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            System.err.println("error opening file: " + filePath);
        }

        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

        String dataLine;
        try(BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            while((dataLine = bufferedReader.readLine()) != null) {
                String[] data = dataLine.split(",");
                facultyList.addFaculty(data);
            }
        } catch (IOException e) {
            System.err.println("error reading file: " + filePath);
        }
        return facultyList;
    }

    @Override
    public void writeData(FacultyList data) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            System.err.println("error writing file: " + filePath);
        }

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        String dataLine;
        try(BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)) {
            for(Faculty faculty : data.getFacultyList()) {
                bufferedWriter.write(faculty.toString());
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            System.err.println("error writing file: " + filePath);
        }
    }
}
