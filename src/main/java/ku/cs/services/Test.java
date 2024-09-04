package ku.cs.services;

import ku.cs.models.user.UserList;
import ku.cs.models.user.exceptions.UserException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {
    private static String dateToFormatString(String format,Date date) {
        String dateString = "NO_DATE";
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            dateString = formatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateString;
    }

    public static void main(String[] args) throws UserException {
        UserListFileDatasource userDataSource = new UserListFileDatasource("data","users.csv");
        String DATE_FORMAT = "yyyy-MM-dd:HH:mm:ss:Z";
        Date date = new Date();
        try {
            UserList users = userDataSource.readData();
            users.addUser("6610402230","b6610402230","student","ศิริสุข","ทานธรรม",dateToFormatString(DATE_FORMAT,date),"sirisuk.t@ku.th","science","computer science","123456789");
            users.addUser("6610402231","b6610402231","student","โนเนม","แอนโนนิมัส",dateToFormatString(DATE_FORMAT,date),"xxxxx.t@ku.th","science","computer science","123456789");
            users.addUser("6610402232","b6610402232","student","โนเนม","แอนโนนิมัส",dateToFormatString(DATE_FORMAT,date),"xxxxx.t@ku.th","science","computer science","123456789");
            users.addUser("6610402233","b6610402233","student","โนเนม","แอนโนนิมัส",dateToFormatString(DATE_FORMAT,date),"xxxxx.t@ku.th","science","computer science","123456789");
            users.addUser("6610402234","b6610402234","student","โนเนม","แอนโนนิมัส",dateToFormatString(DATE_FORMAT,date),"xxxxx.t@ku.th","science","computer science","123456789");
            users.addUser("6610402235","b6610402235","student","โนเนม","แอนโนนิมัส",dateToFormatString(DATE_FORMAT,date),"xxxxx.t@ku.th","science","computer science","123456789");
            users.addUser("0000000000","fscickw","advisor","ชาคริต","วัชโรภาส",dateToFormatString(DATE_FORMAT,date),"fscick.w@ku.th","science","computer science","123456789");
            userDataSource.writeData(users);
            System.out.println(users);

        }catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}

