package info.xiantang.jerrymouse.core.handler;

import info.xiantang.jerrymouse.core.handler.processor.ContentTypeMapper;
import info.xiantang.jerrymouse.core.loader.WebAppLoader;
import info.xiantang.jerrymouse.core.reactor.Reactor;
import info.xiantang.jerrymouse.core.server.ServletWrapper;

import java.io.File;
import java.nio.channels.SocketChannel;
import java.util.Map;

public class ServletContext {

    private Reactor reactor;
    private SocketChannel channel;
    private Map<String, ServletWrapper> mapper;
    private WebAppLoader loader;
    private String jarName;
    private ContentTypeMapper contentTypeMapper;
    private final File jarFile;


    public ServletContext(Reactor reactor, SocketChannel channel, Map<String, ServletWrapper> mapper, WebAppLoader loader, String jarName) {
        this.reactor = reactor;
        this.channel = channel;
        this.mapper = mapper;
        this.loader = loader;
        this.jarName = jarName;
        this.contentTypeMapper = new ContentTypeMapper();
        this.jarFile = new File("build/" + jarName);
    }

    public static ServletContext emptyContext() {
        return new ServletContext(null, null, null, null, null);
    }

    public static ServletContext contextWithMapperAndClassLoader(Map<String, ServletWrapper> mapper, WebAppLoader loader, String jarName) {
        return new ServletContext(null, null, mapper, loader, jarName);
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

    public Map<String, ServletWrapper> getMapper() {
        return mapper;
    }

    public WebAppLoader getLoader() {
        return loader;
    }

    public File getJarFile() {
        return jarFile;
    }


    public ContentTypeMapper getContentTypeMapper() {
        return contentTypeMapper;
    }

}
