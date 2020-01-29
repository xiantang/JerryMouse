package info.xiantang.jerrymouse2.core.server;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class Reactor implements Runnable {

    private Selector selector;
    private ServerSocketChannel serverSocket;
    private Class<? extends BaseHandler> handlerClass;

    public Reactor(int port, Class<? extends BaseHandler> handlerClass) throws IOException {
        this.handlerClass = handlerClass;
        this.selector = Selector.open();
        this.serverSocket = ServerSocketChannel.open();
        this.serverSocket.socket().bind(new InetSocketAddress(port));
        this.serverSocket.configureBlocking(false);
        SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        sk.attach(new Acceptor());
    }


    public void run() {
        try {
            while (!Thread.interrupted()) {
                selector.select();
                Set selected = selector.selectedKeys();
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
                            = handlerClass.getConstructor(Selector.class, SocketChannel.class);
                    handler.newInstance(selector, channel);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    public static class Builder {
        private int port;
        private Class<? extends BaseHandler> handlerClass;

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public Builder setHandlerClass(Class<? extends BaseHandler> handlerClass) {
            this.handlerClass = handlerClass;
            return this;
        }

        public Reactor build() throws IOException {
            return new Reactor(port, handlerClass);
        }
    }


    public static Builder newBuilder() {
        return new Builder();
    }

}
