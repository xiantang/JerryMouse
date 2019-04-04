package info.xiantang.basic.core.handler;

import info.xiantang.basic.core.Servlet;
import info.xiantang.basic.core.WebApp;
import info.xiantang.basic.http.Request;
import info.xiantang.basic.http.Response;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class NioRequestHandler implements Runnable {

    private Servlet servlet;
    private SocketChannel client;
    private Request request;
    private Response response;

    public NioRequestHandler(Request request, Response response, SocketChannel client) throws IOException {
        this.request = request;
        this.response = response;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            Servlet servlet = WebApp.getServletFromUrl(request.getUrl());
            if (servlet != null) {
                servlet.service(request, response);
                response.pushToBrower(200);

            }
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
