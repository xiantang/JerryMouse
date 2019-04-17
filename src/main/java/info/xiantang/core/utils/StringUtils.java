package info.xiantang.core.utils;

/**
 * @Author: xiantang
 * @Date: 2019/4/17 14:45
 */
public class StringUtils {
    public static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
