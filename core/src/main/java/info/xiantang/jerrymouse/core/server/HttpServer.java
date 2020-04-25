package info.xiantang.jerrymouse.core.server;

import info.xiantang.jerrymouse.core.conf.Configuration;
import info.xiantang.jerrymouse.core.lifecycle.LifeCycle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static info.xiantang.jerrymouse.core.utils.JarResourceParser.parseConfigFromJar;

public class HttpServer implements LifeCycle {

    List<ServerSource> sources = new ArrayList<>();
    List<Context> contexts = new ArrayList<>();
    private String rootPath = System.getProperty("user.dir");
    private String jarPath = rootPath + "/build";


    @Override
    public void init() throws Exception {
        loadSources();
        loadContexts();
    }

    @Override
    public void start() {
        for (Context context : contexts) {
            context.start();
        }
    }

    @Override
    public void destroy() throws Exception {
        for (Context context : contexts) {
            context.destroy();
        }
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
            context.init();
            contexts.add(context);
        }
    }


    List<ServerSource> getServerSources() {
        return Collections.unmodifiableList(sources);
    }


    List<Context> getContexts() {
        return contexts;
    }


}
