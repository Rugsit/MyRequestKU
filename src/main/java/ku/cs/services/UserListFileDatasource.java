package ku.cs.services;

import ku.cs.models.user.*;
import ku.cs.models.user.exceptions.UserException;
import ku.cs.services.utils.DateTools;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class UserListFileDatasource implements Datasource<UserList> {
    private String directoryName;
    private String fileName;

    public UserListFileDatasource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkIfFileExist();
    }

    private void checkIfFileExist() {
        File file = new File(directoryName);
        if (!file.exists()) {
            String imageDirectory = directoryName + File.separator + "images";
            File imageDirectoryFile = new File(imageDirectory);
            imageDirectoryFile.mkdirs();
            file = new File(directoryName + File.separator + "users");
            file.mkdir();
            createRootUser();
        }
        // ถ้ามี directory data อยู่แล้ว เช็กว่ามี images และ users ไหม - เผื่อกรณี sub directory หาย
        file = new File(directoryName + File.separator + "images");
        if (!file.exists()) {
            file.mkdir();
        }

        file = new File(directoryName + File.separator + "users");
        if (!file.exists()) {
            file.mkdir();
            createRootUser();
        }

        file = new File(directoryName + File.separator + "images" + File.separator + "users");
        if (!file.exists()) {
            file.mkdir();
        }

        file = new File(directoryName + File.separator + "users" + File.separator + "admin.csv");
        if (!file.exists()) {
            createRootUser();
        }

        String filePath = directoryName + File.separator + "users" + File.separator + fileName;
        file = new File(filePath);


        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Error creating new user file");
            }
        }
    }

    private void createRootUser() {
        String filePath = directoryName + File.separator + "users" + File.separator + "admin.csv";
        File file = new File(filePath);
        try {
            file.createNewFile();
            User root = new Admin("0000000000", "admin", "admin", "admin", "admin", DateTools.localDateTimeToFormatString(User.DATE_FORMAT, LocalDateTime.now()), "no-email", "adminSW211");
            UserListFileDatasource userListDatasource = new UserListFileDatasource("data", "admin.csv");
            userListDatasource.appendData(root);
        } catch (IOException e) {
            System.err.println("Error creating root user");
        } catch (UserException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserList readData() {
        UserList userList = new UserList();
        String filePath = directoryName + File.separator + "users" + File.separator + fileName;
        File file = new File(filePath);

        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.err.println("Error opening user file");
        }

        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);


        String dataLine = "";

        try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            while ((dataLine = bufferedReader.readLine()) != null) {
                if (dataLine.equals("")) continue;

                String[] data = dataLine.split(",");
                for (int i = 0; i < data.length; i++) {
                    data[i] = data[i].trim();
                }
                String uuid = data[0];
                String id = data[1];
                String username = data[2];
                String role = data[3];
                String firstname = data[4];
                String lastname = data[5];
                String lastLogin = data[6];
                String email = data[7];
                String password = data[8];
                String avatar = data[9];
                String activeStatus = data[10];
                String defaultPassword = data[11];
                String faculty = data[12];
                String department = data[13];
                String advisorUUID = data[14];

                userList.addUser(uuid, id, username, role, firstname, lastname, lastLogin, email, password , avatar,activeStatus, defaultPassword, faculty, department, advisorUUID);
            }

        } catch (UserException | IOException e) {
            System.err.println("Error reading user file");
        }

        return userList;
    }


    @Override
    public void writeData(UserList userList) {
        String filePath = directoryName + File.separator + "users" + File.separator + fileName;
        File file = new File(filePath);

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            System.err.println("Error opening user file to write : " + file.getAbsolutePath());
        }

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);

        try (BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)) {
            for (User user : userList.getUsers()) {
                String dataLine = user.toString();

                int maxLength = 15;
                int dataLength = dataLine.split(",").length;
                if(dataLength < maxLength){
                    dataLine += ",none".repeat(maxLength-dataLength);
                }

                bufferedWriter.write(dataLine);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }

        } catch (IOException e) {
            System.err.println("Error writing user data to file");
        }
    }

    public void appendData(User user) {
        String filePath = directoryName + File.separator + "users" + File.separator + fileName;
        File file = new File(filePath);

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file, true);
        } catch (FileNotFoundException e) {
            System.err.println("Error opening file to append : " + file.getAbsolutePath());
        }

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);

        try (BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)) {
            String dataLine = user.toString();

            int maxLength = 15;
            int dataLength = dataLine.split(",").length;
            if(dataLength < maxLength){
                dataLine += ",none".repeat(maxLength-dataLength);
            }

            bufferedWriter.write(dataLine);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            System.err.println("Error appending data to file");
        }
    }

    public UserList readAllUser(){
        UserList userList = new UserList();
        UserListFileDatasource userListDatasource;
        for (UserRoles fileName : UserRoles.values()) {
            userListDatasource = new UserListFileDatasource("data", fileName.toString() + ".csv");
            userList.concatenate(userListDatasource.readData());
        }
        return userList;
    }

}