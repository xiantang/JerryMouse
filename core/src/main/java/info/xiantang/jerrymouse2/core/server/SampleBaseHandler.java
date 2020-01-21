package info.xiantang.jerrymouse2.core.server;

import java.io.EOFException;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class SampleBaseHandler extends BaseHandler implements Runnable {


    public SampleBaseHandler(Selector sel, SocketChannel c) throws IOException {
        super(sel, c);
    }

    public void run() {
        try {
            if (state == READING) read();
            else if (state == SENDING) send();
        } catch (IOException ignored) {
        }
    }

    public void send() throws IOException {
        output.flip();
        if (outputIsComplete()) {
            socket.write(output);
            sk.channel().close();
        }

    }


    public void read() throws IOException {
        input.clear();
        int n = socket.read(input);
        if (inputIsComplete(n)) {
            process();
            sk.interestOps(SelectionKey.OP_WRITE);
        }
    }

    private void process() throws EOFException {
        if (state == CLOSED) {
            throw new EOFException();
        } else if (state == SENDING) {
            // TODO handle
            String requestContent = request.toString(); // 请求内容
            byte[] response = requestContent.getBytes(StandardCharsets.UTF_8);
            output.put(response);
        }
    }


    private boolean inputIsComplete(int bytes) throws IOException {
        if (bytes > 0) {
            input.flip();
            while (input.hasRemaining()) {
                byte ch = input.get();

                if (ch == 3) {
                    state = CLOSED;
                    return true;
                } else if (ch == '\r') {
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

    //TODO 完成代码逻辑
    private boolean outputIsComplete() {
        return true;
    }
}
