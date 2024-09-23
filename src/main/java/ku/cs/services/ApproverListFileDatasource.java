package ku.cs.services;

import ku.cs.models.request.Request;
import ku.cs.models.request.approver.*;
import ku.cs.models.request.approver.exception.ApproverException;

import java.io.*;

public class ApproverListFileDatasource implements Datasource<ApproverList> {
    private String directoryName;
    private String fileName;

    public ApproverListFileDatasource(String type) {
        directoryName = "data" + File.separator + "requests";
        if (type.equals("approver")) {
            this.fileName = "approver-list.csv";
        }
        else {
            this.fileName = "request-approver.csv";
        }
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

    public ApproverList query(String tier, Request request) {
        ApproverList allApproverList = readData();
        ApproverList approverList = new ApproverList();
        if (fileName.equals("request-approver.csv")) {
            for (Approver approver : allApproverList.getApprovers()) {
                if (approver.getTier().equals(tier) && approver.getRequestUUID().equals(request.getUuid())) {
                    approverList.addApprover(approver);
                }
            }
        } else {
            for (Approver approver : allApproverList.getApprovers()) {
                if (approver.getTier().equals(tier)) {
                    approverList.addApprover(approver);
                }
            }
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

    public void appendData(Approver approver, String type) {
        String filePath = null;
        if (type.equals("approver") || type.contains("list")) {
            if (approver.getRequestUUID() != null) {
                return;
            }

            filePath = directoryName + File.separator + "approver-list.csv";
        } else {
            filePath = directoryName + File.separator + "request-approver.csv";
        }

        File file = new File(filePath);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file, true);
        } catch (FileNotFoundException e) {
            System.err.println("Error writing file: " + filePath);
        }
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);

        try(BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)) {
                bufferedWriter.write(approver.toString());
                bufferedWriter.newLine();
                bufferedWriter.flush();
        } catch (IOException e) {
            System.err.println("Error writing file: " + filePath);
        }
    }

    public void appendDataFromList(Approver selectedApprover, Request request) {
        Datasource<ApproverList> datasource = new ApproverListFileDatasource("approver");
        ApproverList approverList = datasource.readData();
        String[] approverData = approverList.findApproverByObject(selectedApprover).toString().split(",");
        String uuid = approverData[0];
        String requestUuid = approverData[1];
        String tier = approverData[2];
        String role = approverData[3];
        String status = approverData[4];
        String signatureFile = approverData[5];
        String firstName = approverData[6];
        String lastName = approverData[7];
        Approver approver = null;
        try {
            if (selectedApprover instanceof FacultyApprover) {
                    approver = new FacultyApprover(uuid, requestUuid, tier, role, status, signatureFile, firstName, lastName);
            } else if (selectedApprover instanceof DepartmentApprover) {
                approver = new DepartmentApprover(uuid, requestUuid, tier, role, status, signatureFile, firstName, lastName);
            } else if (selectedApprover instanceof AdvisorApprover) {
                approver = new AdvisorApprover(uuid, requestUuid, tier, role, status, signatureFile, firstName, lastName);
            } else {
                approver = new OtherApprover(uuid, requestUuid, tier, role, status, signatureFile, firstName, lastName);
            }
        } catch (ApproverException e) {
            throw new RuntimeException(e);
        }

        approver.setRequestUUID(request);
        appendData(approver, "request");
    }
}
