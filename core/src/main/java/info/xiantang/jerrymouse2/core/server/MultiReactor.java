package info.xiantang.jerrymouse2.core.server;

import info.xiantang.jerrymouse2.core.event.Event;
import info.xiantang.jerrymouse2.core.handler.BaseHandler;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class MultiReactor implements Runnable {

    private int subReactorCount;
    private Reactor[] subReactors;
    private Reactor mainReactor;
    private AtomicInteger loadBalancingInteger = new AtomicInteger();


    public MultiReactor(String mainReactorName, int port, Class<? extends BaseHandler> handlerClass, int subReactorCount) throws IOException {
        this.subReactorCount = subReactorCount;
        this.mainReactor = new MainReactorImpl(mainReactorName, port, handlerClass);
        this.subReactors = new Reactor[subReactorCount];
        this.loadBalancingInteger.set(1);
        for (int i = 0; i < subReactorCount; i++) {
            this.subReactors[i] = new SubReactorImpl("subReactor-" + i, Selector.open());
        }

    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public void run() {
        for (int i = 0; i < subReactorCount; i++) {
            new Thread(subReactors[i]).start();
        }
        new Thread(mainReactor).start();
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
        private ServerSocketChannel serverSocket;
        private Class<? extends BaseHandler> handlerClass;

        public Acceptor(ServerSocketChannel serverSocket, Class<? extends BaseHandler> handlerClass) {
            this.serverSocket = serverSocket;
            this.handlerClass = handlerClass;
        }

        @Override
        public void run() {
            SocketChannel channel;
            try {
                channel = serverSocket.accept();
                if (channel != null) {
                    Constructor<? extends BaseHandler> handler
                            = handlerClass.getConstructor(Reactor.class, SocketChannel.class);
                    Reactor subReactor = subReactors[loadBalancingInteger.incrementAndGet() % subReactorCount];
                    handler.newInstance(subReactor, channel);
                }
            } catch (Exception e) {
                //TODO log error
                e.printStackTrace();
            }

        }
    }


    public class MainReactorImpl implements Reactor {
        private final String mainReactorName;
        private Selector selector;

        public MainReactorImpl(String mainReactorName, int port, Class<? extends BaseHandler> handlerClass) throws IOException {
            this.mainReactorName = mainReactorName;
            this.selector = Selector.open();
            ServerSocketChannel serverSocket = ServerSocketChannel.open();
            serverSocket.socket().bind(new InetSocketAddress(port));
            serverSocket.configureBlocking(false);
            SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
            sk.attach(new Acceptor(serverSocket, handlerClass));
        }

        public String getName() {
            return mainReactorName;
        }

        @Override
        public Selector getSelector() {
            return selector;
        }

        @Override
        public void register(Event event) {
            throw new NoSuchMethodError("MainReactor not implement register method.");
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
            Runnable acceptor = (Runnable) (k.attachment());
            if (acceptor != null)
                acceptor.run();
        }
    }


    public class SubReactorImpl implements Reactor {
        private Queue<Event> events = new ConcurrentLinkedQueue<>();
        private Selector subSelector;
        private String name;


        public SubReactorImpl(String name, Selector subSelector) {
            this.name = name;
            this.subSelector = subSelector;
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    Event event;
                    while ((event = events.poll()) != null) {
                        event.event();
                    }
                    subSelector.select();
                    Set selected = subSelector.selectedKeys();
                    for (Object o : selected) dispatch((SelectionKey) o);
                }
            } catch (IOException e) {
                //TODO log error
                e.printStackTrace();
            }
        }

        private void dispatch(SelectionKey k) {
            Event event = (Event) (k.attachment());
            BaseHandler handler = event.getHandler();
            if (handler != null)
                handler.run();
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Selector getSelector() {
            return subSelector;
        }

        @Override
        public void register(Event event) {
            events.offer(event);
            subSelector.wakeup();
        }
    }

}

