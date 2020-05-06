package info.xiantang.jerrymouse.core.server;

import info.xiantang.jerrymouse.core.lifecycle.LifeCycle;
import info.xiantang.jerrymouse.core.loader.ContextLoader;

import java.util.Collections;
import java.util.List;

public class HttpServer implements LifeCycle {
    private List<Context> contexts;
    private List<ServerSource> sources;
    ContextLoader contextLoader;

    public HttpServer() throws Exception {
        this("/build");
    }

    public HttpServer(String buildPath) throws Exception {
        contextLoader = new ContextLoader(buildPath);
        init();
    }

    @Override
    public void init() throws Exception {
        contextLoader.load();
        contexts = contextLoader.getContexts();
        sources = contextLoader.getSources();
    }

    @Override
    public void start() throws Exception {
        for (Context context : contexts) {
            context.init();
            context.start();

        }
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


}
