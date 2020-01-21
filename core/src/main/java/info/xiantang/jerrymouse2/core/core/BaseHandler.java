package info.xiantang.jerrymouse2.core.core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public abstract class BaseHandler {

    private static final int MAXIN = 1024;
    private static final int MAXOUT = 1024;
    final SocketChannel socket;
    final SelectionKey sk;
    static final int READING = 0, SENDING = 1, CLOSED = 2;
    int state = READING;
    ByteBuffer input = ByteBuffer.allocate(MAXIN);
    ByteBuffer output = ByteBuffer.allocate(MAXOUT);
    StringBuilder request = new StringBuilder();

    public BaseHandler(Selector sel, SocketChannel c) throws IOException {
        socket = c;
        c.configureBlocking(false);
        sk = socket.register(sel, 0);
        sk.interestOps(SelectionKey.OP_READ);
        sk.attach(this);
        sel.wakeup();
    }

    public abstract void send() throws IOException;

    public abstract void read() throws IOException;

}
