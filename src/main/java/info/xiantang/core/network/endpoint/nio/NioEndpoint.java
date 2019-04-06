package info.xiantang.core.network.endpoint.nio;

import info.xiantang.core.network.connector.nio.NioAcceptor;
import info.xiantang.core.network.connector.nio.NioPoller;
import info.xiantang.core.network.dispatcher.nio.NioDispatcher;
import info.xiantang.core.network.endpoint.Endpoint;
import info.xiantang.core.network.wrapper.SocketWrapper;
import info.xiantang.core.network.wrapper.nio.NioSocketWrapper;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class NioEndpoint extends Endpoint {
    private ServerSocketChannel server;
    private NioAcceptor acceptor;
    // Poller线程数量是cpu的核数 参考tomcat
    private int pollerCount = Math.min(2, Runtime.getRuntime().availableProcessors());
    private List<NioPoller> nioPollers;
    private NioDispatcher nioDispatcher;

    /**
     * poller轮询器
     */
    private AtomicInteger pollerRotater = new AtomicInteger(0);
    /**


    /**
     * 初始化ServerSocket
     * @param port
     */
    private void initSeverSocket(int port) throws IOException {
        server = ServerSocketChannel.open();
        // 监听地址
        server.bind(new InetSocketAddress(port));
        // 设置阻塞
        server.configureBlocking(true);
        System.out.println("初始化SeverSocket完成");
    }

    /**
     * 初始化initAcceptor
     */
    private void initAcceptor() {
        acceptor = new NioAcceptor(this);
        Thread t = new Thread(acceptor);
        //TODO:setDaemon(false)
//        t.setDaemon(true);
        t.start();
        System.out.println("初始化Acceptor完成");

    }

    /**
     * 初始化initPoller
     * 线程数为CPU核心数目
     */
    private void initPoller() throws IOException {
        nioPollers = new ArrayList<>(pollerCount);
        for (int i = 0; i < pollerCount; i++) {
            String pollName = "NioPoller-" + i;
            NioPoller nioPoller = new NioPoller(this, pollName);
            Thread pollerThread = new Thread(nioPoller);
            pollerThread.setDaemon(true);
            pollerThread.start();

            nioPollers.add(nioPoller);

        }
        System.out.println("初始化Poller完成");
    }

    /**
     * 初始化Dispatcher
     */
    private void initDispatcher() {
        nioDispatcher = new NioDispatcher();
        System.out.println("初始化Dispatcher完成");
    }



    public NioPoller getPoller() {
        int idx = Math.abs(pollerRotater.incrementAndGet()) % nioPollers.size();
        return nioPollers.get(idx);
    }

    public void execute(NioSocketWrapper nioSocketWrapper) throws IOException {
        nioDispatcher.doDispatch(nioSocketWrapper);
    }

    public void registerToPoller(SocketChannel socket,boolean isNewSocket ) throws IOException {
        server.configureBlocking(false);
        getPoller().register(socket,isNewSocket);
        server.configureBlocking(true);
    }


    @Override
    public void start(int port) {
        try {
            initSeverSocket(port);
            initPoller();
            initAcceptor();
            initDispatcher();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public SocketChannel accept() throws IOException {
        return server.accept();
    }


    @Override
    public void close() {

    }
}
