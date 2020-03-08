package info.xiantang.jerrymouse.core.handler;

import info.xiantang.jerrymouse.http.core.HttpRequest;
import info.xiantang.jerrymouse.http.core.HttpResponse;
import info.xiantang.jerrymouse.http.parser.HttpRequestParser;
import org.apache.http.util.ByteArrayBuffer;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;

import static info.xiantang.jerrymouse.core.reactor.Constants.CLOSED;
import static info.xiantang.jerrymouse.core.reactor.Constants.SENDING;

public class HttpHandler extends BaseHandler {
    /**
     * the constructor of handler.
     * we will register channel to selector and  wakeup it
     * and attach the this object prepare to use.
     *
     * @param context
     */
    public HttpHandler(HandlerContext context) {
        super(context);
    }

    @Override
    public boolean inputIsComplete(ByteBuffer input, ByteArrayBuffer rawRequest, int bytes) throws IOException {
        if (bytes > 0) {
            input.flip();
            while (input.hasRemaining()) {
                byte ch = input.get();
                if (ch == 3) {
                    setState(CLOSED);
                    return true;
                } else {
                    rawRequest.append((char) ch);
                }
            }
        } else if (bytes == -1) {
            throw new EOFException();
        }

        return true;
    }

    @Override
    public void process(ByteBuffer output, ByteArrayBuffer rawRequest) throws Exception {
        int state = getState();
        if (state == CLOSED) {
            throw new EOFException();
        } else if (state == SENDING) {
            HttpRequestParser parser = new HttpRequestParser(rawRequest.toByteArray(), HttpRequest.newBuilder());
            HttpRequest request = parser.parse();
            HttpResponse response = new HttpResponse();
            HttpProcessor processor = new HttpProcessor(getContext());
            processor.process(request, response);
            output.put(response.toRawResponse());
        }
    }


}
