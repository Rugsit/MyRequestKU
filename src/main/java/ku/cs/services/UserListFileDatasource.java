package ku.cs.services;

import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.models.user.exceptions.UserException;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class UserListFileDatasource implements Datasource<UserList>{
    private String directoryName;
    private String fileName;

    public UserListFileDatasource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkIfFileExist();
    }

    private void checkIfFileExist(){
        File file = new File(directoryName);
        if (!file.exists()){
            String imageDirectory = directoryName + File.separator + "images";
            File imageDirectoryFile = new File(imageDirectory);
            imageDirectoryFile.mkdirs();
            imageDirectory = directoryName + File.separator + "images" + File.separator + "users";
            file = new File(directoryName + File.separator + "users");
            file.mkdir();

        }
        // ถ้ามี directory data อยู่แล้ว เช็กว่ามี images และ users ไหม - เผื่อกรณี sub directory หาย
        file = new File(directoryName + File.separator + "images");
        if (!file.exists()){
            file.mkdir();
        }

        file = new File(directoryName + File.separator + "users");
        if (!file.exists()){
            file.mkdir();
        }

        file = new File(directoryName + File.separator + "images" + File.separator + "users");
        if (!file.exists()){
            file.mkdir();
        }

        String filePath = directoryName + File.separator + "users" + File.separator + fileName;
        file = new File(filePath);
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error creating new user file");
            }
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
            System.out.println("Error opening user file");
        }

        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);


        String dataLine = "";

        try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)){
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
                String lastLogin  = data[6];
                String email = data[7];
                String faculty = data[8];
                String department = data[9];
                String password = data[10];
                String avatar = data[11];

                userList.addUser(uuid, id, username, role, firstname, lastname, lastLogin, email, faculty, department, password, avatar);
            }

        } catch (UserException | IOException e) {
            System.out.println("Error reading user file");
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
            throw new RuntimeException(e);
        }

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);

        try (BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)){
            for (User user : userList.getUsers()) {
                String dataLine = user.toString();
                bufferedWriter.write(dataLine);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
