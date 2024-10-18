package ku.cs.services.utils;

public class StringCompare {
    public static Boolean isDigit(String str) {
        return str.matches("\\d+");
    }
    public static Boolean isAplha(String str) {
        //INCLUDE THAI
        return str.matches("[a-zA-Z\u0E00-\u0E7F-_\\s]+");
    }
    public static Boolean isAlphaNumberic(String str) {
        //INCLUDE THAI
        return str.matches("[a-zA-Z0-9\u0E00-\u0E7F-_]+");
    }
    public static Boolean haveSpace(String str) {
        return str.matches(".*\\s.*");
    }
    public static Boolean startWithSpace(String str) {
        return str.matches("^\\s+.*");
    }
    public static Boolean endWithSpace(String str) {
        return str.matches(".*\\s+$");
    }
    public static Boolean haveDuplicateSpace(String str) {
        return str.matches(".*\\s{2,}.*");
    }
    public static Boolean isValidEmailPattern(String str) {
        if(str.equals("no-email")) return true;
        return str.matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
    }
}
