package info.xiantang.jerrymouse.core.server;

import info.xiantang.jerrymouse.core.conf.Configuration;

public class FakeServer extends HttpServer {
    private final int port;
    public FakeServer(int port) {
        this.port = port;
    }

    void loadContexts() throws Exception {
        for (ServerSource serverSource : sources) {
            Configuration config = serverSource.getConfig();
            config.setPort(port);
            String jarName = serverSource.getJarName();
            Context context = new Context(jarName, config);
            context.init();
            contexts.add(context);
        }
    }

}
