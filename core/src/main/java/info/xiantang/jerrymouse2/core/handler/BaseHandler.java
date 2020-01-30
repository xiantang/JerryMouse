package info.xiantang.jerrymouse2.core.handler;

import info.xiantang.jerrymouse2.core.server.MultiReactor;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static info.xiantang.jerrymouse2.core.server.Constants.*;


public abstract class BaseHandler implements Runnable {

    private MultiReactor reactor;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(CORE_NUM);
    protected final SocketChannel socket;
    protected final SelectionKey sk;
    private final Selector selector;
    private int state = READING;
    protected ByteBuffer input = ByteBuffer.allocate(BUFFER_MAX_IN);
    protected ByteBuffer output = ByteBuffer.allocate(BUFFER_MAX_OUT);
    protected StringBuilder request = new StringBuilder();


    /**
     * the constructor of handler.
     * we will register channel to selector and  wakeup it
     * and attach the this object prepare to use.
     *
     * @param channel
     * @throws IOException
     */
    public BaseHandler(MultiReactor reactor, SocketChannel channel) throws IOException {
        this.reactor = reactor;
        this.socket = channel;
        this.selector = reactor.getMainSelector();
        channel.configureBlocking(false);
        this.sk = socket.register(selector, 0);
        sk.interestOps(SelectionKey.OP_READ);
        sk.attach(this);
        selector.wakeup();
    }

    protected void send() throws IOException {
        output.flip();
        socket.write(output);
        sk.channel().close();
    }

    public abstract boolean inputIsComplete(int bytes) throws IOException;

    public abstract void process(ByteBuffer output) throws EOFException;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getReactorName() {
        return reactor.getMainReactorName();
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


    private synchronized void processAndHandOff() throws EOFException {
        state = SENDING;
        process(output);
        sk.interestOps(SelectionKey.OP_WRITE);
        selector.wakeup();
    }

    protected synchronized void read() throws IOException {
        input.clear();
        int n = socket.read(input);
        if (inputIsComplete(n)) {
            state = PROCESSING;
            threadPool.execute(new Processor());
        }
    }

    class Processor implements Runnable {

        @Override
        public void run() {
            try {
                processAndHandOff();
            } catch (EOFException e) {
                // print error
                e.printStackTrace();
            }
        }
    }
}
