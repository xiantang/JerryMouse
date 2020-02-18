package info.xiantang.jerrymouse.core.handler;

import info.xiantang.jerrymouse.core.reactor.Reactor;
import info.xiantang.jerrymouse.http.servlet.Servlet;

import java.nio.channels.SocketChannel;
import java.util.Map;

public class HandlerContext {
    private Reactor reactor;
    private SocketChannel channel;
    private Map<String, Servlet> mapper;

    public HandlerContext(Reactor reactor, SocketChannel channel, Map<String, Servlet> mapper) {
        this.reactor = reactor;
        this.channel = channel;
        this.mapper = mapper;
    }

    public static HandlerContext emptyContext() {
        return new HandlerContext(null, null, null);
    }

    public static HandlerContext contextOnlyHaveMapper(Map<String, Servlet> mapper) {
        return new HandlerContext(null, null, mapper);
    }

    public Reactor getReactor() {
        return reactor;
    }

    public void setReactor(Reactor subReactor) {
        this.reactor = subReactor;
    }

    public SocketChannel getChannel() {
        return channel;
    }

    public void setChannel(SocketChannel channel) {
        this.channel = channel;
    }

    public Map<String, Servlet> getMapper() {
        return mapper;
    }


}
