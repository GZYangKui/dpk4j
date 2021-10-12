package cn.navclub.xt.server.util;

public class StringUtil {
    private static final String EMPTY = "";

    public static boolean isEmpty(String str) {
        return str == null || str.trim().equals(EMPTY);
    }

    public static boolean nonEmpty(String str){
        return !isEmpty(str);
    }
}
