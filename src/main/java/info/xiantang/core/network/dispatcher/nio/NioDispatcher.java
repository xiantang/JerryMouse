package info.xiantang.core.network.dispatcher.nio;

import info.xiantang.core.handler.NioRequestHandler;
import info.xiantang.core.network.dispatcher.AbstractDispatcher;
import info.xiantang.core.exception.ServletException;
import info.xiantang.core.http.Request;
import info.xiantang.core.http.Response;
import info.xiantang.core.network.wrapper.SocketWrapper;
import info.xiantang.core.network.wrapper.nio.NioSocketWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioDispatcher extends AbstractDispatcher {
    @Override
    public void doDispatch(SocketWrapper socketWrapper) throws IOException {
        NioSocketWrapper nioSocketWrapper = (NioSocketWrapper) socketWrapper;
        SocketChannel socketChannel = nioSocketWrapper.getSocketChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        Request request = null;
        Response response = new Response(socketChannel);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            while (socketChannel.read(buffer) > 0) {
                buffer.flip();
                baos.write(buffer.array());
            }
            baos.close();
            request = new Request(baos.toByteArray());
            pool.execute(new NioRequestHandler(nioSocketWrapper,request,response));
        } catch (IOException e) {
            e.printStackTrace();
            nioSocketWrapper.close();

        } catch (ServletException e) {

        }


    }
}
