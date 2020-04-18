package info.xiantang.jerrymouse.core.utils;

import java.util.regex.Pattern;

public class StaticResourcesUtils {
    public static boolean verifyPath(String path) {
        String pattern = ".*(ico|png|jpg|html)$";
        return Pattern.matches(pattern, path);
    }
}
