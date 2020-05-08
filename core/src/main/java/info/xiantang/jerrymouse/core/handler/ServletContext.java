package info.xiantang.jerrymouse.core.handler;

import info.xiantang.jerrymouse.core.handler.processor.ContentTypeMapper;
import info.xiantang.jerrymouse.core.loader.WebAppLoader;
import info.xiantang.jerrymouse.core.pipeline.BoundPipeline;
import info.xiantang.jerrymouse.core.pipeline.ContentOutBoundPipeline;
import info.xiantang.jerrymouse.core.pipeline.RootBoundPipeline;
import info.xiantang.jerrymouse.core.pipeline.SessionInBoundPipeline;
import info.xiantang.jerrymouse.core.reactor.MultiReactor;
import info.xiantang.jerrymouse.core.reactor.Reactor;
import info.xiantang.jerrymouse.core.server.ServletWrapper;
import info.xiantang.jerrymouse.http.session.Session;

import java.io.File;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServletContext {

    private Reactor reactor;
    private SocketChannel channel;
    private Map<String, ServletWrapper> mapper;
    private WebAppLoader loader;
    private ContentTypeMapper contentTypeMapper;
    private final File jarFile;
    private BoundPipeline inBoundPipeline;
    private BoundPipeline outBoundPipeline;

    private Map<String, Session> sessionMap = new ConcurrentHashMap<>();
    private MultiReactor.Acceptor acceptor;


    public ServletContext(Reactor reactor, SocketChannel channel, Map<String, ServletWrapper> mapper, WebAppLoader loader, String jarName) {
        this.reactor = reactor;
        this.channel = channel;
        this.mapper = mapper;
        this.loader = loader;
        this.contentTypeMapper = new ContentTypeMapper();
        this.jarFile = new File("build/" + jarName);
        init();
    }

    private void init() {
        BoundPipeline rootInBoundPipeline = new RootBoundPipeline(this);
        BoundPipeline sessionBoundPipeline = new SessionInBoundPipeline(this);
        rootInBoundPipeline.setNext(sessionBoundPipeline);
        setInboundPipeline(rootInBoundPipeline);
        BoundPipeline rootOutBoundPipeline = new RootBoundPipeline(this);
        BoundPipeline contentOutBoundPipeline = new ContentOutBoundPipeline(this);
        rootOutBoundPipeline.setNext(contentOutBoundPipeline);
        setOutboundPipeline(rootOutBoundPipeline);

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

    public void setInboundPipeline(BoundPipeline boundPipeline) {
        this.inBoundPipeline = boundPipeline;
    }

    public BoundPipeline getInboundPipeline() {
        return inBoundPipeline;
    }

    public Map<String, Session> getSessionMap() {
        return sessionMap;
    }

    public void setOutboundPipeline(BoundPipeline outBoundPipeline) {
        this.outBoundPipeline = outBoundPipeline;
    }
    public BoundPipeline getOutBoundPipeline() {
        return outBoundPipeline;
    }

    public void setAcceptor(MultiReactor.Acceptor acceptor) {
        this.acceptor = acceptor;
    }

    public MultiReactor.Acceptor getAcceptor() {
        return acceptor;
    }
}
