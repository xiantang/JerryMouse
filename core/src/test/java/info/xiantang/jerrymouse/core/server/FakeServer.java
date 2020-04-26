package info.xiantang.jerrymouse.core.server;

import info.xiantang.jerrymouse.core.conf.Configuration;

import java.util.List;

public class FakeServer extends HttpServer {
    private final List<Integer> ports;

    public FakeServer(List<Integer> ports) {
        this.ports = ports;
    }


    void loadContexts() throws Exception {

        for (int i = 0; i < sources.size(); i++) {
            Configuration config = sources.get(i).getConfig();
            config.setPort(ports.get(i));
            String jarName = sources.get(i).getJarName();
            Context context = new Context(jarName, config);
            context.init();
            contexts.add(context);
        }
    }

}
