package info.xiantang.jerrymouse2.core.server;

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

    private final SocketChannel socket;
    private final SelectionKey sk;
    private final Selector selector;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(CORE_NUM);
    int state = READING;
    ByteBuffer input = ByteBuffer.allocate(BUFFER_MAX_IN);
    ByteBuffer output = ByteBuffer.allocate(BUFFER_MAX_OUT);
    StringBuilder request = new StringBuilder();


    /**
     * the constructor of handler.
     * we will register channel to selector and  wakeup it
     * and attach the this object prepare to use.
     *
     * @param selector
     * @param channel
     * @throws IOException
     */
    public BaseHandler(Selector selector, SocketChannel channel) throws IOException {
        socket = channel;
        this.selector = selector;
        channel.configureBlocking(false);
        sk = socket.register(selector, 0);
        sk.interestOps(SelectionKey.OP_READ);
        sk.attach(this);
        selector.wakeup();
    }

    private void send() throws IOException {
        output.flip();
        socket.write(output);
        sk.channel().close();
    }

    public abstract boolean inputIsComplete(int bytes) throws IOException;

    public abstract void process() throws EOFException;


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
        process();
        sk.interestOps(SelectionKey.OP_WRITE);
        selector.wakeup();
    }

    private synchronized void read() throws IOException {
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
