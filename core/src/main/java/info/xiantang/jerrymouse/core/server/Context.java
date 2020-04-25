package info.xiantang.jerrymouse.core.server;

import info.xiantang.jerrymouse.core.conf.Configuration;
import info.xiantang.jerrymouse.core.handler.HandlerContext;
import info.xiantang.jerrymouse.core.handler.HttpHandler;
import info.xiantang.jerrymouse.core.lifecycle.LifeCycle;
import info.xiantang.jerrymouse.core.loader.JarClassLoader;
import info.xiantang.jerrymouse.core.loader.WebAppLoader;
import info.xiantang.jerrymouse.core.reactor.MultiReactor;

import java.io.IOException;
import java.util.Map;

public class Context implements LifeCycle {
    private int port;
    private Map<String, ServletWrapper> mapper;
    private String jarName;
    private Configuration configuration;
    private MultiReactor reactor;
    private WebAppLoader webAppLoader;

    @Override
    public void init() throws Exception {
        loadOnStartUpServlet();
    }


    @Override
    public void start() {
        new Thread(reactor).start();
    }


    @Override
    public void destroy() throws IOException {
        this.reactor.stop();
    }


    public Context(String jarName, Configuration configuration) throws Exception {
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
                .setHandlerContext(HandlerContext.contextWithMapperAndClassLoader(mapper, webAppLoader,jarName))
                .setSubReactorCount(subReactorNum)
                .build();

    }

    Map<String, ServletWrapper> getMapper() {
        return mapper;
    }

    private void loadOnStartUpServlet() throws Exception {
        webAppLoader.loadOnStartUp();
    }


}
