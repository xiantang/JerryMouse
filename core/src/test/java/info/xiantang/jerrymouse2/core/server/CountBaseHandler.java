package info.xiantang.jerrymouse2.core.server;

import java.io.EOFException;
import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import static info.xiantang.jerrymouse2.core.server.Constants.*;

class CountBaseHandler extends BaseHandler {


    public CountBaseHandler(Selector selector, SocketChannel channel) throws IOException {
        super(selector, channel);
    }


    @Override
    public void process() throws EOFException {
        if (state == CLOSED) {
            throw new EOFException();
        } else if (state == SENDING) {
            output.put("1".getBytes());
        }
    }


    @Override
    public boolean inputIsComplete(int bytes) throws IOException {
        if (bytes > 0) {
            input.flip(); // 切换成读取模式
            while (input.hasRemaining()) {
                byte ch = input.get();
                if (ch == 3) {
                    state = CLOSED;
                    return true;
                } else if (ch == '\r') { // continue
                } else if (ch == '\n') {
                    state = SENDING;
                    return true;
                } else {
                    request.append((char) ch);
                }
            }
        } else if (bytes == -1) {
            throw new EOFException();
        }

        return false;
    }

}