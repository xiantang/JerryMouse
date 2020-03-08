package info.xiantang.jerrymouse.core.server;

import info.xiantang.jerrymouse.core.conf.Configuration;
import info.xiantang.jerrymouse.core.handler.HandlerContext;
import info.xiantang.jerrymouse.core.handler.HttpHandler;
import info.xiantang.jerrymouse.core.loader.JarClassLoader;
import info.xiantang.jerrymouse.core.loader.WebAppLoader;
import info.xiantang.jerrymouse.core.reactor.MultiReactor;

import java.util.Map;

public class Context {
    private int port;
    private Map<String, ServletWrapper> mapper;
    private String jarName;
    private Configuration configuration;
    private MultiReactor reactor;
    private WebAppLoader webAppLoader;


    Context(String jarName, Configuration configuration) throws Exception {
        this.jarName = jarName;
        this.configuration = configuration;
        this.mapper = configuration.getRouter();
        this.port = configuration.getPort();
        int subReactorNum = configuration.getSubReactorNum();
        JarClassLoader jarClassLoader = new JarClassLoader(jarName);
        this.webAppLoader = new WebAppLoader(mapper, jarClassLoader);
        this.reactor = MultiReactor.newBuilder()
                .setPort(port)
                .setHandlerClass(HttpHandler.class)
                .setMainReactorName("MainReactor")
                .setHandlerContext(HandlerContext.contextWithMapperAndClassLoader(mapper, jarClassLoader))
                .setSubReactorCount(subReactorNum)
                .build();
        loadOnStartUpServlet();
    }


    private void loadOnStartUpServlet() throws Exception {
        webAppLoader.loadOnStartUp();
    }


    public void start() {
        new Thread(reactor).start();
    }

    Map<String, ServletWrapper> getMapper() {
        return mapper;
    }
}
