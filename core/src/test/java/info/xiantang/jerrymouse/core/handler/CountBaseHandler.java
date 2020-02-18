package info.xiantang.jerrymouse.core.handler;

import org.apache.http.util.ByteArrayBuffer;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;

import static info.xiantang.jerrymouse.core.reactor.Constants.CLOSED;
import static info.xiantang.jerrymouse.core.reactor.Constants.SENDING;

public class CountBaseHandler extends BaseHandler {


    public CountBaseHandler(HandlerContext context) {
        super(context);
    }
    @Override
    public boolean inputIsComplete(ByteBuffer input, ByteArrayBuffer request, int bytes) throws IOException {
        if (bytes > 0) {
            input.flip(); // 切换成读取模式
            while (input.hasRemaining()) {
                byte ch = input.get();
                if (ch == 3) {
                    setState(CLOSED);
                    return true;
                } else if (ch == '\r') { // continue
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
    public void process(ByteBuffer output, ByteArrayBuffer request) throws EOFException {
        int state = getState();
        if (state == CLOSED) {
            throw new EOFException();
        } else if (state == SENDING) {
            output.put("1".getBytes());
        }
    }

}