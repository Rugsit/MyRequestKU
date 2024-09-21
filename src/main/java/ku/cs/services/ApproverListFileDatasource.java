package ku.cs.services;

import ku.cs.models.request.approver.Approver;
import ku.cs.models.request.approver.ApproverList;
import ku.cs.models.request.approver.exception.ApproverException;

import java.io.*;

public class ApproverListFileDatasource implements Datasource<ApproverList> {
    private String directoryName;
    private String fileName;

    public ApproverListFileDatasource() {
        directoryName = "data" + File.separator + "requests";
        this.fileName = "request-approver.csv";
        checkIfFileExist();
    }
    private void checkIfFileExist() {
        String filePath = directoryName;
        File file = new File(filePath);
        if (file.exists()) {
            file.mkdirs();
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
    public ApproverList readData() {
        ApproverList approverList = new ApproverList();
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
            while ((dataLine = bufferedReader.readLine()) != null) {
                String[] data = dataLine.split(",");
                String uuid = data[0];
                String requestUuid = data[1];
                String tier = data[2];
                String role = data[3];
                String status = data[4];
                String signatureFile = data[5];
                String firstName = data[6];
                String lastName = data[7];

                approverList.addApprover(uuid, requestUuid, tier, role, status, signatureFile, firstName, lastName);
            }
        } catch (IOException | ApproverException e) {
            System.err.println("Error reading file: " + filePath);
        }

        return approverList;
    }

    @Override
    public void writeData(ApproverList data) {
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
            for (Approver approver : data.getApprovers()) {
                bufferedWriter.write(approver.toString());
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            System.err.println("Error writing file: " + filePath);
        }
    }

}
