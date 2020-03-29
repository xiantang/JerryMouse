package info.xiantang.jerrymouse.core.utils;

import info.xiantang.jerrymouse.core.conf.Configuration;
import info.xiantang.jerrymouse.core.utils.FileUtils;
import info.xiantang.jerrymouse.core.utils.JsonConfigReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarResourceParser {
    private static final String CONFIG_NAME = "config.json";
    public static Configuration parseConfigFromJar(File file) throws IOException {
        JarFile jarFile = new JarFile(file.getAbsolutePath());
        Enumeration<JarEntry> entries = jarFile.entries();
        JarEntry configEntry = null;
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String name = jarEntry.getName();
            if (CONFIG_NAME.equals(name)) {
                configEntry = jarEntry;
                break;
            }
        }
        if (configEntry == null) return null;
        InputStream input = jarFile.getInputStream(configEntry);
        String process = FileUtils.readFromStream(input);
        JsonConfigReader reader = new JsonConfigReader();
        return reader.parseStringAsConfiguration(process);
    }
}
