package ku.cs.services.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTools {
    public static LocalDateTime formatToLocalDateTime(String format, String dateString) {
//        SimpleDateFormat formatter = new SimpleDateFormat(format);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDateTime date = null;
        try {
            date = LocalDateTime.parse(dateString, formatter);
        } catch (Exception e){
            e.printStackTrace();
        }
        return date;
    }

    public static String localDateTimeToFormatString(String format, LocalDateTime date) {
        String dateString = "NO_DATE";
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            dateString = date.format(formatter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateString;
    }
}
