package info.xiantang.basic.core.handler;

import info.xiantang.basic.core.Servlet;
import info.xiantang.basic.core.WebApp;
import info.xiantang.basic.http.Request;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class NioRequestHandler implements Runnable {

    private Servlet servlet;

    public NioRequestHandler(Request request, SocketChannel socketChannel) throws IOException {

        System.out.println(request.getUrl());
        socketChannel.close();
    }

    @Override
    public void run() {

    }
}
