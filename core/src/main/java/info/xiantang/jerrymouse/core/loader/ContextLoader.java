package info.xiantang.jerrymouse.core.loader;

import info.xiantang.jerrymouse.core.conf.Configuration;
import info.xiantang.jerrymouse.core.server.Context;
import info.xiantang.jerrymouse.core.server.ServerSource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static info.xiantang.jerrymouse.core.utils.JarResourceParser.parseConfigFromJar;

public class ContextLoader {

    private String rootPath = System.getProperty("user.dir");
    private String jarPath;
    List<ServerSource> sources = new ArrayList<>();
    List<Context> contexts = new ArrayList<>();

    public ContextLoader(String buildPath) {
        jarPath = rootPath + buildPath;
    }

    public List<ServerSource> getSources() {
        return sources;
    }

    public List<Context> getContexts() {
        return contexts;
    }

    public void load() throws Exception {
        loadSources();
        loadContexts();
    }

    void loadSources() throws IOException {
        File buildDir = new File(jarPath);
        File[] files;
        if (buildDir.isDirectory() && (files = buildDir.listFiles()) != null) {
            for (File file : files) {
                Configuration configuration = parseConfigFromJar(file);
                if (configuration == null) continue;
                ServerSource serverSource = new ServerSource(file.getName(), configuration);
                sources.add(serverSource);
                System.out.println("加载jar包" + file.getName() + "中的资源");
            }
        }
    }

    void loadContexts() throws Exception {
        for (ServerSource serverSource : sources) {
            Configuration config = serverSource.getConfig();
            String jarName = serverSource.getJarName();
            Context context = new Context(jarName, config);
            contexts.add(context);
        }
    }

}
