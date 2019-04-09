package info.xiantang.core.handler;

import info.xiantang.core.network.endpoint.nio.NioEndpoint;
import info.xiantang.core.network.wrapper.SocketWrapper;
import info.xiantang.core.network.wrapper.nio.NioSocketWrapper;
import info.xiantang.core.context.WebApp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class NioRequestHandler implements Runnable {


    private HttpServlet servlet;
    private SocketChannel client;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private NioEndpoint endpoint;
    private NioSocketWrapper nioSocketWrapper;

    public NioRequestHandler(SocketWrapper socketWrapper, HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.nioSocketWrapper = (NioSocketWrapper)socketWrapper;
        this.request = request;
        this.response = response;
        this.endpoint =(NioEndpoint) nioSocketWrapper.getServer();
        this.client = nioSocketWrapper.getSocketChannel();
    }

    @Override
    public void run() {
        try {
             servlet = (HttpServlet) WebApp.getServletFromUrl(request.getRequestURI());
            if (servlet != null) {
                servlet.service(request, response);
//              TODO:  response.flushResponse(200); 这句是什么意思
                // ANS 推送到浏览器
            }
            if (request.getParameter("connection") == null || !request.getParameter("connection").equals("false")) {
            // 反注册
                endpoint.registerToPoller(client, false);
            } else nioSocketWrapper.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();

        }

    }
}
