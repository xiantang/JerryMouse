package info.xiantang.core;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.EOFException;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;


public class ReactorTest {


    static class CountBaseHandler extends BaseHandler implements Runnable {


        public CountBaseHandler(Selector sel, SocketChannel c) throws IOException {
            super(sel, c);
        }

        @Override
        public void send() throws IOException {
            output.flip();
            socket.write(output);
            sk.channel().close();
        }

        @Override
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
                output.put("1".getBytes());
            }
        }


        private boolean inputIsComplete(int bytes) throws IOException {
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

        @Override
        public void run() {
            try {
                if (state == READING) read();
                else if (state == SENDING) send();
            } catch (IOException ignored) {
            }
        }
    }

    @Test
    public void canAcceptRequests() throws IOException {
        Reactor reactor = Reactor.newBuilder()
                .setPort(9864)
                .setHandlerClass(CountBaseHandler.class)
                .build();
        Thread reactorT = new Thread(reactor);
        reactorT.start();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://localhost:9864");
        CloseableHttpResponse response = httpClient.execute(httpget);
        HttpEntity httpEntity = response.getEntity();
        String strResult = EntityUtils.toString(httpEntity);
        assert "1".equals(strResult);

    }
}
