package info.xiantang.jerrymouse.core.server;

import info.xiantang.jerrymouse.core.conf.Configuration;
import info.xiantang.jerrymouse.core.handler.ServletContext;
import info.xiantang.jerrymouse.core.handler.HttpHandler;
import info.xiantang.jerrymouse.core.lifecycle.LifeCycle;
import info.xiantang.jerrymouse.core.loader.JarClassLoader;
import info.xiantang.jerrymouse.core.loader.WebAppLoader;
import info.xiantang.jerrymouse.core.reactor.MultiReactor;

import java.io.IOException;
import java.util.Map;

public class Context implements LifeCycle {
    private final int subReactorNum;
    private int port;
    private Map<String, ServletWrapper> mapper;
    private String jarName;
    private Configuration configuration;
    private MultiReactor reactor;
    private WebAppLoader webAppLoader;

    @Override
    public void init() throws Exception {
        loadOnStartUpServlet();
        this.reactor = MultiReactor.newBuilder()
                .setPort(port)
                .setHandlerClass(HttpHandler.class)
                .setMainReactorName("MainReactor")
                .setServletContext(ServletContext.contextWithMapperAndClassLoader(mapper, webAppLoader,jarName))
                .setSubReactorCount(subReactorNum)
                .build();
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
        subReactorNum = configuration.getSubReactorNum();
        JarClassLoader jarClassLoader = new JarClassLoader(jarName);
        this.webAppLoader = new WebAppLoader(mapper, jarClassLoader);


    }

    Map<String, ServletWrapper> getMapper() {
        return mapper;
    }

    private void loadOnStartUpServlet() throws Exception {
        webAppLoader.loadOnStartUp();
    }

    public void setPort(int port) {
        this.port = port;
    }


}
