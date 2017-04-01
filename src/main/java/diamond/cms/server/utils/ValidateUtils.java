package diamond.cms.server.utils;

public class ValidateUtils {
    public static boolean isEmail(String str) {
        if (str == null) return false;
        String regex = "\\w+(\\.\\w)*@\\w+(\\.\\w{2,3}){1,3}";
        return str.matches(regex);
    }
}
