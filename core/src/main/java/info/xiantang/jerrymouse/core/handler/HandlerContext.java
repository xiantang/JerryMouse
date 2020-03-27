package info.xiantang.jerrymouse.core.handler;

import info.xiantang.jerrymouse.core.loader.WebAppLoader;
import info.xiantang.jerrymouse.core.reactor.Reactor;
import info.xiantang.jerrymouse.core.server.ServletWrapper;

import java.nio.channels.SocketChannel;
import java.util.Map;

public class HandlerContext {
    private Reactor reactor;
    private SocketChannel channel;
    private Map<String, ServletWrapper> mapper;
    private WebAppLoader loader;

    public HandlerContext(Reactor reactor, SocketChannel channel, Map<String, ServletWrapper> mapper, WebAppLoader loader) {
        this.reactor = reactor;
        this.channel = channel;
        this.mapper = mapper;
        this.loader = loader;
    }

    public static HandlerContext emptyContext() {
        return new HandlerContext(null, null, null, null);
    }

    public static HandlerContext contextWithMapperAndClassLoader(Map<String, ServletWrapper> mapper, WebAppLoader loader) {
        return new HandlerContext(null, null, mapper, loader);
    }

    public Reactor getReactor() {
        return reactor;
    }

    public void setReactor(Reactor subReactor) {
        this.reactor = subReactor;
    }

    SocketChannel getChannel() {
        return channel;
    }

    public void setChannel(SocketChannel channel) {
        this.channel = channel;
    }

    Map<String, ServletWrapper> getMapper() {
        return mapper;
    }

    public WebAppLoader getLoader() {
        return loader;
    }

}
