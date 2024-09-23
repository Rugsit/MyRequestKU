package ku.cs.services;

import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class RequestListFileDatasource implements Datasource<RequestList> {
    private final String directoryName;

    public RequestListFileDatasource(String directoryName) {
        this.directoryName = directoryName;
        checkIfFileExist();
    }

    private void checkIfFileExist() {
        File file = new File(directoryName + File.separator + "requests");
        if (!file.exists()) {
            file.mkdirs();
        }

        file = new File(directoryName + File.separator + "requests" + File.separator + "requests.csv");
        if (!file.exists()) {
            try {
                file.createNewFile();

            } catch (IOException e) {
                System.err.println("Error creating request list file");
            }
        }

        file = new File(directoryName + File.separator + "requests" + File.separator + "requests-log.csv");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Error creating request log file");
            }
        }
    }

    @Override
    public RequestList readData() {
        RequestList requestList = new RequestList();
        String filePath = directoryName + File.separator + "requests" + File.separator + "requests.csv";
        File file = new File(filePath);

        FileInputStream fileInputStream = null;
        try {
             fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.err.println("Error opening request list file");
        }

        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

        String dataLine = "";
        try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            while ((dataLine = bufferedReader.readLine()) != null) {
                String[] data = dataLine.split(",");
                requestList.addRequest(data);
            }
        } catch (IOException e) {
            System.err.println("Error reading request list file");
        }
        return requestList;
    }

    @Override
    public void writeData(RequestList data) {
        String filePath = directoryName + File.separator + "requests" + File.separator + "requests.csv";
        File file = new File(filePath);
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            System.err.println("Error opening request list file");
        }

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);

        try (BufferedWriter bufferedWriter = new  BufferedWriter(outputStreamWriter)){
            for (Request request : data.getRequests()) {
                bufferedWriter.write(request.toString());
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            System.err.println("Error writing request list file");
        }
    }

    public void appendData(Request request, String option) {
        String filePath;
        if (option.equals("data")) {
            filePath = directoryName + File.separator + "requests" + File.separator + "requests.csv";
        } else if (option.equals("log")) {
            filePath = directoryName + File.separator + "requests" + File.separator + "requests-log.csv";
        } else {
            throw new IllegalArgumentException("Unknown option: " + option);
        }
        File file = new File(filePath);
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file, true);
        } catch (FileNotFoundException e) {
            System.err.println("Error opening request list file");
        }

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
        try {
            BufferedWriter bufferedWriter = new  BufferedWriter(outputStreamWriter);
            bufferedWriter.write(request.toString());
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            System.err.println("Error writing request list file");
        }

    }

    public void appendToLog(Request request) {
        Datasource<RequestList> datasource = new RequestListFileDatasource("data");
        RequestList requestList = datasource.readData();

        for (Request req : requestList.getRequests()) {
            if (req.getUuid().equals(request.getUuid())) {
                appendData(request, "log");
                break;
            }
        }
    }

    public RequestList queryLog(Request request) {
        RequestList requestList = new RequestList();
        requestList.addRequest(request);
        String filePath = directoryName + File.separator + "requests" + File.separator + "requests-log.csv";
        File file = new File(filePath);

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.err.println("Error opening request logs file");
        }

        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

        String dataLine = "";
        try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            while ((dataLine = bufferedReader.readLine()) != null) {
                String[] data = dataLine.split(",");
                if (request.getUuid().toString().equals(data[1])) {
                    requestList.addRequest(data);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading request list file");
        }
        return requestList;
    }
}
