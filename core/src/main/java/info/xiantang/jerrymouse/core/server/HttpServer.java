package info.xiantang.jerrymouse.core.server;

import info.xiantang.jerrymouse.core.handler.HandlerContext;
import info.xiantang.jerrymouse.core.handler.HttpHandler;
import info.xiantang.jerrymouse.core.reactor.MultiReactor;
import info.xiantang.jerrymouse.http.servlet.Servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpServer {
    //TODO need read from conf
    private int port;
    private Map<String, Servlet> mapper = new HashMap<>();
    private MultiReactor reactor;


    public HttpServer(int port, Map<String, Servlet> mapper) throws IOException {
        this.port = port;
        this.mapper.putAll(mapper);
        this.reactor = MultiReactor.newBuilder()
                .setPort(port)
                .setHandlerClass(HttpHandler.class)
                .setMainReactorName("MainReactor")
                .setHandlerContext(HandlerContext.contextOnlyHaveMapper(mapper))
                .setSubReactorCount(3)
                .build();

    }


    public void start() {
        reactor.run();
    }
}
