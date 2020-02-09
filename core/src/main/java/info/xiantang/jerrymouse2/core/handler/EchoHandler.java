package info.xiantang.jerrymouse2.core.handler;

import org.apache.http.util.ByteArrayBuffer;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.charset.StandardCharsets;

import static info.xiantang.jerrymouse2.core.reactor.Constants.*;

public class EchoHandler extends BaseHandler {

    /**
     * the constructor of handler.
     * we will register channel to selector and  wakeup it
     * and attach the this object prepare to use.
     *
     * @param context
     */
    public EchoHandler(HandlerContext context) {
        super(context);
    }

    @Override
    protected void send() throws IOException {
        int written = -1;
        output.flip();
        if (output.hasRemaining()) {
            written = socket.write(output);
        }
        if (outputIsComplete(written)) {
            sk.channel().close();
        } else {
            setState(READING);
            socket.write(ByteBuffer.wrap("\r\nreactor> ".getBytes()));
            sk.interestOps(SelectionKey.OP_READ);
        }

    }

    @Override
    public boolean inputIsComplete(ByteBuffer input, ByteArrayBuffer request, int bytes) throws IOException {
        if (bytes > 0) {
            input.flip();
            while (input.hasRemaining()) {
                byte ch = input.get();
                if (ch == 3) {
                    setState(CLOSED);
                    return true;
                } else if (ch == '\r') {
                } else if (ch == '\n') {
                    setState(SENDING);
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

    @Override
    public void process(ByteBuffer output, ByteArrayBuffer rawRequest) throws EOFException {
        int state = getState();
        if (state == CLOSED) {
            throw new EOFException();
        } else if (state == SENDING) {
            String requestContent = new String(rawRequest.toByteArray());
            byte[] response = requestContent.getBytes(StandardCharsets.UTF_8);
            output.put(response);
        }
    }

    private boolean outputIsComplete(int written) {
        if (written <= 0) {
            return true;
        }
        output.clear();
        rawRequest.clear();
        return false;
    }

}
