package info.xiantang.core.utils;

import info.xiantang.core.http.HttpResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Date;

public class SocketOutputStream extends Writer{
    private SocketChannel socketChannel;
    private HttpResponse httpResponse;

    public SocketOutputStream(SocketChannel socketChannel, HttpResponse httpResponse) {
        this.socketChannel = socketChannel;
        this.httpResponse  = httpResponse;
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        byte[] btuf = new byte[len - off];
        for (int i = 0, j = off; j < len; i++, j++) {
            btuf[i] = (byte) cbuf[j];
        }
        ByteBuffer buffer = ByteBuffer.wrap(btuf, off, len);
        //待完善
    }

    @Override
    public void flush() throws IOException {
        httpResponse.setStatus(200);
        httpResponse.setHeader("Date", String.valueOf(new Date()));
        httpResponse.setHeader("Server", "X Server/0.0.1;charset=UTF-8");
        httpResponse.setHeader("Content-Type", "text/html");
        System.out.println(httpResponse.toString());
        ByteBuffer buffer = ByteBuffer.wrap(httpResponse.toString().getBytes());
        System.out.println(buffer.array().length);
        System.out.println(socketChannel.write(buffer));
        System.out.println("写入完成");
    }

    @Override
    public void close() throws IOException {
        //管道没有这个方法
    }
}
