package info.xiantang.jerrymouse.core.utils;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;

public class FileUtils {
    public static URL[] parseSinglePathToUrls(String path) throws Exception {
        String property = System.getProperty("user.dir");
        URL url = new URL("file:"+property + "/build/"+path);
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

    public static String readFromStream(InputStream input) throws IOException {
        InputStreamReader isr = new InputStreamReader(input);
        BufferedReader reader = new BufferedReader(isr);
        String line;
        StringBuilder builder = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            builder.append(line).append("\n");
        }
        reader.close();
        return builder.toString();
    }


}

