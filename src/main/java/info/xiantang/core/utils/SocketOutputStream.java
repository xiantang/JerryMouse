package info.xiantang.core.utils;

import info.xiantang.core.http.HttpResponse;

import java.io.IOException;
import java.io.Writer;
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
        httpResponse.getBodyBuffer().put(btuf);

        //待完善
//        socketChannel.write(buffer);
    }

    @Override
    public void flush() throws IOException {
        httpResponse.setStatus(200);
        httpResponse.setHeader("Date", String.valueOf(new Date()));
        httpResponse.setHeader("Server", "X Server/0.0.1;charset=UTF-8");
        httpResponse.setHeader("Content-Type", "text/html");
        httpResponse.setHeader("Content-Length", "122");
        System.out.println(httpResponse.toString());
        ByteBuffer buffer = ByteBuffer.wrap(httpResponse.toString().getBytes());
        socketChannel.write(buffer);
        ByteBuffer aaaa = ByteBuffer.wrap(("fhjskdhfnsdfnsdflksjfnmlksdgnvdlfb" +
                "dasdasklfndklgfnfglkdfnghlkfdhnlbnldkgndflkgndfklgdfngfdsgfdg"+
                "dasdasklfndklgfnfglkdfnghlkfdhnlbnldkgndflkgndfklgdfngfdsgfdg").getBytes());
        socketChannel.write(aaaa);



    }

    @Override
    public void close() throws IOException {
        //管道没有这个方法
    }
}
