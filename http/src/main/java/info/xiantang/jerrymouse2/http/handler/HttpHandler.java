package info.xiantang.jerrymouse2.http.handler;

import info.xiantang.jerrymouse2.core.handler.BaseHandler;
import info.xiantang.jerrymouse2.core.reactor.Reactor;
import org.apache.http.util.ByteArrayBuffer;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static info.xiantang.jerrymouse2.core.reactor.Constants.CLOSED;
import static info.xiantang.jerrymouse2.core.reactor.Constants.SENDING;

public class HttpHandler extends BaseHandler {
    /**
     * the constructor of handler.
     * we will register channel to selector and  wakeup it
     * and attach the this object prepare to use.
     *
     * @param reactor
     * @param channel
     */
    public HttpHandler(Reactor reactor, SocketChannel channel) {
        super(reactor, channel);
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
    public void process(ByteBuffer output, ByteArrayBuffer rawRequest) throws EOFException {
        int state = getState();
        if (state == CLOSED) {
            throw new EOFException();
        } else if (state == SENDING) {
            String rawResponse = new String(rawRequest.toByteArray());
            System.out.println(rawResponse);
            output.put(buildResponse(rawResponse).getBytes());
        }
    }

    private String buildResponse(String request) {
        return "HTTP/1.1 200 OK\r\n" +
                "Date: Mon, 27 Jul 2009 12:28:53 GMT\n" +
                "Server: Apache/2.2.14 (Win32)\n" +
                "Last-Modified: Wed, 22 Jul 2009 19:15:56 GMT\n" +
                "Content-Length: " + request.length() + "\n" +
                "Content-Type: text/html\n" +
                "Connection: Closed\r\n\n" +
                request;
    }


}
