package com.github.apachefoundation.jerrymouse2;

import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @Author: xiantang
 * @Date: 2019/9/9 20:36
 */
public class NioEndpoint extends AbstractEndpoint {

    private ServerSocketChannel serverSock;

    @Override
    public void bind() throws Exception {
        initServerSocket();
    }


    private void initServerSocket() throws Exception {
        serverSock = ServerSocketChannel.open();
        // TODO 从配置文件读取
        serverSock.bind(new InetSocketAddress(8080));
        // 如果我们将configureBlocking(false)
        // 就表示acceptor 是非阻塞的接受连接
        // accept() 方法立刻返回 null 会产生
        // 忙读的情况
        serverSock.configureBlocking(true);
    }



    @Override
    public void startInternal() {
        if (!running) {
            running = true;
        }
        acceptor = new Acceptor(this);
        String threadName = "Acceptor";
        Thread acceptorThread = new Thread(acceptor, threadName);
        acceptorThread.start();
    }

    @Override
    protected SocketChannel serverSocketAccept() throws Exception {
        return serverSock.accept();
    }

    @Override
    public void stopInternal() {
        if (!paused) {
            pause();
        }
        if (running) {
            running = false;
        }
    }
}
