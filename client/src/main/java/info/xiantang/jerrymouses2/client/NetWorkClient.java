package info.xiantang.jerrymouses2.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NetWorkClient {
    public static String doRequest(String host,int port,String content) throws IOException {
        InetSocketAddress servAddr = new InetSocketAddress(host, port);
        SocketChannel socket = SocketChannel.open(servAddr);
        byte[] msg = content.getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(msg);
        ByteBuffer out = ByteBuffer.allocate(10);

        socket.write(buffer);
        buffer.clear();
        socket.read(out);
        socket.close();
        return new String(out.array()).trim();
    }
}
