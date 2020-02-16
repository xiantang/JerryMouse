package info.xiantang.jerrymouse2.core.utils;

import java.net.URL;

public class FileUtils {
    public static URL[] parseSinglePathToUrls(String path) {
        URL url = FileUtils.class.getClassLoader().getResource(path);
        return new URL[]{url};
    }
}
