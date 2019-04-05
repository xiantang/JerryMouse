package info.xiantang.core.handler;

import info.xiantang.core.network.endpoint.nio.NioEndpoint;
import info.xiantang.core.network.wrapper.SocketWrapper;
import info.xiantang.core.network.wrapper.nio.NioSocketWrapper;
import info.xiantang.core.servlet.Servlet;
import info.xiantang.core.context.WebApp;
import info.xiantang.core.http.Request;
import info.xiantang.core.http.Response;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class NioRequestHandler implements Runnable {

    private Servlet servlet;
    private SocketChannel client;
    private Request request;
    private Response response;
    private NioEndpoint endpoint;
    private NioSocketWrapper nioSocketWrapper;

    public NioRequestHandler(SocketWrapper socketWrapper, Request request, Response response) throws IOException {
        this.nioSocketWrapper = (NioSocketWrapper)socketWrapper;
        this.request = request;
        this.response = response;
        this.endpoint =(NioEndpoint) nioSocketWrapper.getServer();
        this.client = nioSocketWrapper.getSocketChannel();
    }

    @Override
    public void run() {
        try {
             servlet = WebApp.getServletFromUrl(request.getUrl());
            if (servlet != null) {
                servlet.service(request, response);
                response.flushResponse(200);
            }
            if (request.getConnetction().equals("keep-alive")) {
                endpoint.registerToPoller(client, false);
            } else nioSocketWrapper.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
