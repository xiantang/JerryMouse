package info.xiantang.core.handler;


import info.xiantang.core.exception.RequestInvalidException;
import info.xiantang.core.http.HttpRequest;
import info.xiantang.core.http.HttpResponse;
import info.xiantang.core.network.connector.nio.NioPoller;
import info.xiantang.core.network.endpoint.nio.NioEndpoint;
import info.xiantang.core.network.wrapper.SocketWrapper;
import info.xiantang.core.network.wrapper.nio.NioSocketWrapper;
import info.xiantang.core.context.WebApp;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class NioRequestHandler implements Runnable {

    private HttpServlet servlet;
    private SocketChannel client;
    private NioEndpoint endpoint;
    private NioSocketWrapper nioSocketWrapper;
    private Logger logger = Logger.getLogger(NioRequestHandler.class);

    public NioRequestHandler(SocketWrapper socketWrapper) throws IOException {
        this.nioSocketWrapper = (NioSocketWrapper)socketWrapper;
        SocketChannel socketChannel = nioSocketWrapper.getSocketChannel();
        this.endpoint =(NioEndpoint) nioSocketWrapper.getServer();
        this.client = nioSocketWrapper.getSocketChannel();
    }

    @Override
    public void run() {
        try {

            SocketChannel socketChannel = nioSocketWrapper.getSocketChannel();
            HttpServletRequest request = new HttpRequest(socketChannel);
            HttpServletResponse response = new HttpResponse(socketChannel);
            nioSocketWrapper.setResponse(response);
            nioSocketWrapper.setRequest(request);

            servlet = (HttpServlet) WebApp.getServletFromUrl(request.getRequestURI());
            if (servlet != null) {
                servlet.service(request, response);
            }


            logger.info("开始注册写事件");
            endpoint.registerToPoller(client, false, SelectionKey.OP_WRITE, nioSocketWrapper);
            logger.info("写事件完成注册");

            //关闭之后才可以完全奖body写入
            response.getWriter().close();

            /* 我注释了这段 改为了注册写事件 写完再注册下一个读事件 见NioResponseHandler
            if (request.getParameter("connection") == null || !request.getParameter("connection").equals("false")) {
                endpoint.registerToPoller(client, false);
            }
            else
                nioSocketWrapper.close();*/

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();

        } catch (RequestInvalidException e) {
            nioSocketWrapper.close();
        }

    }
}