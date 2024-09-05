package ku.cs.services.utils;

public class StringCompare {
    public static Boolean isDigit(String str) {
        return str.matches("\\d+");
    }
    public static Boolean isAplha(String str) {
        //INCLUDE THAI
        return str.matches("[a-zA-Z\u0E00-\u0E7F]+");
    }
    public static Boolean isAlphaNumberic(String str) {
        //INCLUDE THAI
        return str.matches("[a-zA-Z0-9\u0E00-\u0E7F]+");
    }
    public static Boolean haveSpace(String str) {
        return str.matches("\\s+");
    }
    public static Boolean isValidEmailPattern(String str) {
        //MAY NOT WORK AS EXPECTED
        return str.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }
}
