package ku.cs.services;

import ku.cs.models.department.Department;
import ku.cs.models.department.DepartmentList;

import java.io.*;

public class DepartmentListFileDatasource implements Datasource<DepartmentList> {
    private String directoryName;
    private String fileName;

    public DepartmentListFileDatasource(String directoryName) {
        this.directoryName = directoryName;
        this.fileName = "department.csv";
        checkIfFileExist();
    }
    private void checkIfFileExist() {
        String filePath = directoryName;
        File file = new File(filePath);
        if (file.exists()) {
            file.mkdir();
        }

        filePath = directoryName + File.separator + fileName;
        file = new File(filePath);
        if (!file.exists()) {
            try{
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Error creating file: " + filePath);
            }
        }
    }
    @Override
    public DepartmentList readData() {
        DepartmentList departmentList = new DepartmentList();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.err.println("Error opening file: " + filePath);
        }

        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        String dataLine;
        try(BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            dataLine = bufferedReader.readLine();
            String[] data = dataLine.split(",");
            departmentList.addDepartment(data);
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
        }
        return departmentList;
    }

    @Override
    public void writeData(DepartmentList data) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            System.err.println("Error writing file: " + filePath);
        }
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);

        try(BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)) {
            for (Department department : data.getDepartments()) {
                bufferedWriter.write(department.toString());
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            System.err.println("Error writing file: " + filePath);
        }
    }
}
