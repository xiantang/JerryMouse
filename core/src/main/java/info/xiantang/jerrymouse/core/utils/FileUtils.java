package info.xiantang.jerrymouse.core.utils;

import com.google.common.primitives.Bytes;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FileUtils {
    public static URL[] parseSinglePathToUrls(String path) throws Exception {
        String property = System.getProperty("user.dir");
        URL url = new URL("file:"+property + "/build/"+path);
        return new URL[]{url};
    }

   public static byte[] readBytes(File image) throws IOException {
        List<Byte> bytes = new ArrayList<>();
        try (InputStream inputStream = new FileInputStream(image)) {
            int read;
            while ((read = inputStream.read()) != -1) {
                byte newByte = (byte) read;
                bytes.add(newByte);
            }
        }
        return Bytes.toArray(bytes);
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

    static String readStringFromStream(InputStream input) throws IOException {
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


    public static byte[] readFileFromJar(File file, String path) throws IOException {
        JarFile jarFile = new JarFile(file.getAbsolutePath());
        Enumeration<JarEntry> entries = jarFile.entries();
        JarEntry configEntry = null;
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String name = jarEntry.getName();
            if (path.equals(name)) {
                configEntry = jarEntry;
                break;
            }
        }
        if (configEntry == null) return null;
        InputStream input = jarFile.getInputStream(configEntry);
        byte[] buffer = new byte[4096];
        int n;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }
}

