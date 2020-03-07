package info.xiantang.jerrymouse.core.server;

import info.xiantang.jerrymouse.core.conf.Configuration;
import info.xiantang.jerrymouse.core.handler.HandlerContext;
import info.xiantang.jerrymouse.core.handler.HttpHandler;
import info.xiantang.jerrymouse.core.reactor.MultiReactor;

import java.io.IOException;
import java.util.Map;

public class Context {
    private int port;
    private Map<String, ServletWrapper> mapper;
    private String jarName;
    private Configuration configuration;
    private MultiReactor reactor;


    public Context(String jarName, Configuration configuration) throws IOException {
        this.jarName = jarName;
        this.configuration = configuration;
        this.mapper = configuration.getRouter();
        this.port = configuration.getPort();
        int subReactorNum = configuration.getSubReactorNum();
        this.reactor = MultiReactor.newBuilder()
                .setPort(port)
                .setHandlerClass(HttpHandler.class)
                .setMainReactorName("MainReactor")
                .setHandlerContext(HandlerContext.contextOnlyHaveMapper(mapper))
                .setSubReactorCount(subReactorNum)
                .build();

    }


    public void start() {
        reactor.run();
    }

    public Map<String, ServletWrapper> getMapper() {
        return mapper;
    }
}
