package info.xiantang.basic.core.network.dispatcher.nio;

import info.xiantang.basic.core.handler.NioRequestHandler;
import info.xiantang.basic.core.network.dispatcher.AbstractDispatcher;
import info.xiantang.basic.http.Request;
import info.xiantang.basic.http.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioDispatcher extends AbstractDispatcher {
    @Override
    public void doDispatch(SocketChannel client) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
//        System.out.println("开始读取Request");
        Request request = null;
        Response response = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            while (client.read(buffer) > 0) {
                buffer.flip();
                baos.write(buffer.array());
            }
            baos.close();
            request = new Request(baos.toByteArray());
            pool.execute(new NioRequestHandler(request,client));
        } catch (IOException e) {
            e.printStackTrace();

        }


    }
}
