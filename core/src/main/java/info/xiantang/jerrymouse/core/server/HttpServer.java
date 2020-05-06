package info.xiantang.jerrymouse.core.server;

import info.xiantang.jerrymouse.core.lifecycle.LifeCycle;
import info.xiantang.jerrymouse.core.loader.ContextLoader;
import info.xiantang.jerrymouse.core.scanner.FileScanner;
import info.xiantang.jerrymouse.core.scanner.listener.FileModifyListener;
import info.xiantang.jerrymouse.core.scanner.listener.Listener;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class HttpServer implements LifeCycle {
    private final FileScanner fileScanner;
    private List<Context> contexts;
    private List<ServerSource> sources;
    private final ContextLoader contextLoader;

    public HttpServer() throws Exception {
        this("/build");
    }

    public HttpServer(String buildPath) throws Exception {
        contextLoader = new ContextLoader(buildPath, this);
        File file = new File(buildPath.replaceFirst("/", ""));
        fileScanner = new FileScanner(file,100);
        init();
    }

    public void setContexts(List<Context> contexts) {
        this.contexts = contexts;
    }

    public void setSources(List<ServerSource> sources) {
        this.sources = sources;
    }

    @Override
    public void init() throws Exception {
        contextLoader.load();
        Listener listener = new FileModifyListener(contextLoader);
        fileScanner.registerListener(listener);
    }

    @Override
    public void start() throws Exception {
        for (Context context : contexts) {
            context.init();
            context.start();
        }
        new Thread(fileScanner).start();

    }

    @Override
    public void destroy() throws Exception {
        for (Context context : contexts) {
            context.destroy();
        }
    }


    List<ServerSource> getServerSources() {
        return Collections.unmodifiableList(sources);
    }


    public List<Context> getContexts() {
        return contextLoader.getContexts();
    }


    public void clear() throws IOException {

        for (Context context : contexts) {
            context.destroy();
        }
        contexts.clear();
        sources.clear();
    }
}
