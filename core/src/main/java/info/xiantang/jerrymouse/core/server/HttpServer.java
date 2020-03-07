package info.xiantang.jerrymouse.core.server;

import info.xiantang.jerrymouse.core.conf.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static info.xiantang.jerrymouse.core.compile.JarResourceParser.parseConfigFromJar;

public class HttpServer {

    private List<ServerSource> sources = new ArrayList<>();
    private String rootPath = System.getProperty("user.dir");
    private String jarPath = rootPath + "/build";

    public void init() throws IOException {
        loadSources();
    }

    private void loadSources() throws IOException {
        File buildDir = new File(jarPath);
        File[] files;
        if (buildDir.isDirectory() && (files = buildDir.listFiles()) != null) {
            for (File file : files) {
                Configuration configuration = parseConfigFromJar(file);
                if(configuration == null) continue;
                ServerSource serverSource = new ServerSource(file.getName(), configuration);
                sources.add(serverSource);
            }
        }
    }


    public List<ServerSource> getServerSources() {
        return Collections.unmodifiableList(sources);
    }
}
