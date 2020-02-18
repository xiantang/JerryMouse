package info.xiantang.jerrymouse.core.handler;

import info.xiantang.jerrymouse.core.event.Event;
import info.xiantang.jerrymouse.core.reactor.Reactor;
import org.apache.http.util.ByteArrayBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static info.xiantang.jerrymouse.core.reactor.Constants.*;


public abstract class BaseHandler implements Runnable {

    private static ExecutorService threadPool = Executors.newFixedThreadPool(CORE_NUM);
    protected final SocketChannel socket;
    private final Selector selector;
    private final HandlerContext context;
    protected SelectionKey sk;
    protected ByteBuffer input = ByteBuffer.allocate(BUFFER_MAX_IN);
    protected ByteBuffer output = ByteBuffer.allocate(BUFFER_MAX_OUT);
    protected ByteArrayBuffer rawRequest = new ByteArrayBuffer(0);
    private Reactor reactor;
    private int state = READING;


    /**
     * the constructor of handler.
     * we will register channel to selector and  wakeup it
     * and attach the this object prepare to use.
     */
    public BaseHandler(HandlerContext context) {
        this.context = context;
        this.reactor = context.getReactor();
        this.socket = context.getChannel();
        this.selector = reactor.getSelector();
        HandlerEvent event = new HandlerEvent(this);
        reactor.register(event);
    }

    public int getState() {
        return state;
    }

    public HandlerContext getContext() {
        return context;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getReactorName() {
        return reactor.getName();
    }

    public void run() {
        try {
            if (state == READING)
                read();
            else if (state == SENDING)
                send();
        } catch (IOException e) {
            // TODO use log
            e.printStackTrace();
        }
    }

    protected synchronized void read() throws IOException {
        input.clear();
        int n = socket.read(input);
        if (inputIsComplete(input, rawRequest, n)) {
            state = PROCESSING;
            threadPool.execute(new Processor());
        }
    }

    protected void send() throws IOException {
        output.flip();
        socket.write(output);
        sk.channel().close();
    }

    public abstract boolean inputIsComplete(ByteBuffer input, ByteArrayBuffer rawRequest, int bytes) throws IOException;

    private synchronized void processAndHandOff() throws Exception {
        state = SENDING;
        process(output, rawRequest);
        sk.interestOps(SelectionKey.OP_WRITE);
        selector.wakeup();
    }

    public abstract void process(ByteBuffer output, ByteArrayBuffer request) throws Exception;

    class Processor implements Runnable {

        @Override
        public void run() {
            try {
                processAndHandOff();
            } catch (Exception e) {
                //TODO print error
                e.printStackTrace();
            }
        }
    }

    public class HandlerEvent implements Event {
        private BaseHandler handler;

        public HandlerEvent(BaseHandler handler) {
            this.handler = handler;
        }

        @Override
        public void event() throws IOException {
            socket.configureBlocking(false);
            handler.sk = socket.register(selector, 0);
            sk.interestOps(SelectionKey.OP_READ);
            sk.attach(this);
        }

        public BaseHandler getHandler() {
            return handler;
        }
    }
}
