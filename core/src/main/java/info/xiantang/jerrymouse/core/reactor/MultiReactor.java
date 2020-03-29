package info.xiantang.jerrymouse.core.reactor;

import info.xiantang.jerrymouse.core.event.Event;
import info.xiantang.jerrymouse.core.handler.BaseHandler;
import info.xiantang.jerrymouse.core.handler.HandlerContext;

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
    private HandlerContext handlerContext;

    private MultiReactor(String mainReactorName, int port, Class<? extends BaseHandler> handlerClass, int subReactorCount, HandlerContext context) throws IOException {
        this.subReactorCount = subReactorCount;
        this.mainReactor = new MainReactorImpl(mainReactorName, port, handlerClass);
        this.subReactors = new Reactor[subReactorCount];
        this.loadBalancingInteger.set(1);
        this.handlerContext = context == null ? HandlerContext.emptyContext() : context;
        this.handlerContext.setResourcePath("src/main/resources");
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

    public void stop() throws IOException {
        mainReactor.stop();
        for (int i = 0; i < subReactorCount; i++) {
            subReactors[i].stop();
        }
    }


    public static class Builder {
        private int port;
        private Class<? extends BaseHandler> handlerClass;
        private String mainReactorName = "DefaultMainReactor";
        private int subReactorCount;
        private HandlerContext handlerContext;

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
            return new MultiReactor(mainReactorName, port, handlerClass, subReactorCount, handlerContext);
        }


        public Builder setSubReactorCount(int count) {
            this.subReactorCount = count;
            return this;
        }

        public Builder setHandlerContext(HandlerContext handlerContext) {
            this.handlerContext = handlerContext;
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

        Acceptor(ServerSocketChannel serverSocket, Class<? extends BaseHandler> handlerClass) {
            this.serverSocket = serverSocket;
            this.handlerClass = handlerClass;
        }

        @Override
        public void run() {
            SocketChannel channel;
            try {
                channel = serverSocket.accept();
                if (channel != null) {
                    newInstanceOfHandler(channel);
                }
            } catch (Exception e) {
                //TODO log error
                e.printStackTrace();
            }

        }

        void stop() throws IOException {
            serverSocket.close();
        }

        private void newInstanceOfHandler(SocketChannel channel) throws Exception {
            Constructor<? extends BaseHandler> handler
                    = handlerClass.getConstructor(HandlerContext.class);
            Reactor subReactor = subReactors[loadBalancingInteger.incrementAndGet() % subReactorCount];
            handlerContext.setChannel(channel);
            handlerContext.setReactor(subReactor);
            handler.newInstance(handlerContext);
        }
    }


    public class MainReactorImpl implements Reactor {
        private final String mainReactorName;
        private Selector selector;
        private volatile boolean isRunning = true;
        private Acceptor acceptor;


        MainReactorImpl(String mainReactorName, int port, Class<? extends BaseHandler> handlerClass) throws IOException {
            this.mainReactorName = mainReactorName;
            this.selector = Selector.open();
            ServerSocketChannel serverSocket = ServerSocketChannel.open();
            serverSocket.socket().bind(new InetSocketAddress(port));
            serverSocket.configureBlocking(false);
            SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
            acceptor = new Acceptor(serverSocket, handlerClass);
            sk.attach(acceptor);

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
                while (!Thread.interrupted() && isRunning) {
                    selector.select();
                    Set selected = selector.selectedKeys();
                    for (Object o : selected) dispatch((SelectionKey) o);
                    selected.clear();
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

        public void stop() throws IOException {
            isRunning = false;
            acceptor.stop();
        }
    }


    public static class SubReactorImpl implements Reactor {
        private Queue<Event> events = new ConcurrentLinkedQueue<>();
        private Selector subSelector;
        private String name;
        private boolean isRunning = true;


        SubReactorImpl(String name, Selector subSelector) {
            this.name = name;
            this.subSelector = subSelector;
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted() && isRunning) {
                    Event event;
                    while ((event = events.poll()) != null) {
                        event.event();
                    }
                    subSelector.select();
                    Set<SelectionKey> selected = subSelector.selectedKeys();
                    for (SelectionKey key : selected) dispatch(key);
                    selected.clear();
                }
            } catch (IOException e) {
                //TODO log error
                e.printStackTrace();
            }
        }

        private void dispatch(SelectionKey key) {
            Event event = (Event) (key.attachment());
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

        @Override
        public void stop() {
            isRunning = false;
        }
    }

}

