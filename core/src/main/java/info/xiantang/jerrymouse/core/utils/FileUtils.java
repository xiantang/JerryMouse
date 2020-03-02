package info.xiantang.jerrymouse.core.utils;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;

public class FileUtils {
    public static URL[] parseSinglePathToUrls(String path) {
        URL url = FileUtils.class.getClassLoader().getResource(path);
        return new URL[]{url};
    }

    public static void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (! Files.isSymbolicLink(f.toPath())) {
                    deleteDir(f);
                }
            }
        }
        file.delete();
    }
}
