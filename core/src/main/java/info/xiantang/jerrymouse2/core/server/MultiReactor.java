package info.xiantang.jerrymouse2.core.server;

import info.xiantang.jerrymouse2.core.handler.BaseHandler;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class MultiReactor implements Runnable {

    private int subReactorCount;
    private String mainReactorName;
    private Selector mainSelector;
    private ServerSocketChannel serverSocket;
    private Class<? extends BaseHandler> handlerClass;
    private MultiReactor reactor = this;

    public MultiReactor(String mainReactorName, int port, Class<? extends BaseHandler> handlerClass, int subReactorCount) throws IOException {
        this.mainReactorName = mainReactorName;
        this.subReactorCount = subReactorCount;
        this.handlerClass = handlerClass;
        this.mainSelector = Selector.open();
        this.serverSocket = ServerSocketChannel.open();
        this.serverSocket.socket().bind(new InetSocketAddress(port));
        this.serverSocket.configureBlocking(false);
        SelectionKey sk = serverSocket.register(mainSelector, SelectionKey.OP_ACCEPT);
        sk.attach(new Acceptor());
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                mainSelector.select();
                Set selected = mainSelector.selectedKeys();
                for (Object o : selected) dispatch((SelectionKey) o);
            }
        } catch (IOException e) {
            //TODO log error
            e.printStackTrace();
        }
    }

    private void dispatch(SelectionKey k) {
        Runnable r = (Runnable) (k.attachment());
        if (r != null)
            r.run();
    }

    public String getMainReactorName() {
        return mainReactorName;
    }

    public Selector getMainSelector() {
        return mainSelector;
    }

    public static class Builder {
        private int port;
        private Class<? extends BaseHandler> handlerClass;
        private String mainReactorName = "DefaultMainReactor";
        private int subReactorCount;

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public Builder setHandlerClass(Class<? extends BaseHandler> handlerClass) {
            this.handlerClass = handlerClass;
            return this;
        }

        public Builder setMainReactorName(String name) {
            this.mainReactorName = name;
            return this;
        }

        public MultiReactor build() throws IOException {
            return new MultiReactor(mainReactorName, port, handlerClass, subReactorCount);
        }


        public Builder setSubReactorCount(int count) {
            this.subReactorCount = count;
            return this;
        }
    }

    /**
     * this is the endpoint of the reactor
     * if acceptor got a connection form the
     * client will create a certain handler and
     * will gain a instance by reflect.
     */
    public class Acceptor implements Runnable {
        public void run() {
            SocketChannel channel;
            try {
                channel = serverSocket.accept();
                if (channel != null) {
                    Constructor<? extends BaseHandler> handler
                            = handlerClass.getConstructor(MultiReactor.class, SocketChannel.class);
                    handler.newInstance(reactor, channel);
                }
            } catch (Exception e) {
                //TODO log error
                e.printStackTrace();
            }

        }
    }


    public class MainReactor implements Runnable {
        private final String mainReactorName;
        private final int port;
        private Selector mainSelector;
        private ServerSocketChannel serverSocket;

        public MainReactor(String mainReactorName, int port) throws IOException {
            this.mainReactorName = mainReactorName;
            this.port = port;
            this.mainSelector = Selector.open();
            ServerSocket socket = serverSocket.socket();
            socket.bind(new InetSocketAddress(port));
            this.serverSocket.configureBlocking(false);
            SelectionKey sk = serverSocket.register(mainSelector, SelectionKey.OP_ACCEPT);
            sk.attach(new Acceptor());
        }

        public void run() {
            try {
                while (!Thread.interrupted()) {
                    mainSelector.select();
                    Set selected = mainSelector.selectedKeys();
                    for (Object o : selected) dispatch((SelectionKey) o);
                }
            } catch (IOException e) {
                //TODO log error
                e.printStackTrace();
            }
        }

        private void dispatch(SelectionKey k) {
            Runnable r = (Runnable) (k.attachment());
            if (r != null)
                r.run();
        }
    }


    public class SubReactor implements Runnable {

        private Selector selector;
        public SubReactor(Selector selector) {
            this.selector = selector;

        }
        @Override
        public void run() {

        }
    }

}

