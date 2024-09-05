package ku.cs.services.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTools {
    public static Date formatToDate(String format,String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            System.err.println("Error : ParseException\n\tformatToDate method returns null!");
        }
        return date;
    }

    public static String dateToFormatString(String format, Date date) {
        String dateString = "NO_DATE";
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            dateString = formatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateString;
    }
}
