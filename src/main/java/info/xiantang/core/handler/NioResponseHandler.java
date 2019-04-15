package info.xiantang.core.handler;

import info.xiantang.core.context.WebApp;
import info.xiantang.core.network.endpoint.nio.NioEndpoint;
import info.xiantang.core.network.wrapper.SocketWrapper;
import info.xiantang.core.network.wrapper.nio.NioSocketWrapper;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class NioResponseHandler implements Runnable {

    private SocketChannel client;
    private NioEndpoint endpoint;
    private NioSocketWrapper nioSocketWrapper;
    private  Logger logger = Logger.getLogger(WebApp.class);
    public NioResponseHandler(SocketWrapper socketWrapper) throws IOException {
        this.nioSocketWrapper = (NioSocketWrapper)socketWrapper;
        this.endpoint =(NioEndpoint) nioSocketWrapper.getServer();
        this.client = nioSocketWrapper.getSocketChannel();
    }

    @Override
    public void run() {
        SocketChannel socketChannel = nioSocketWrapper.getSocketChannel();
        HttpServletResponse response = nioSocketWrapper.getResponse();
        HttpServletRequest request = nioSocketWrapper.getRequest();

        try {
            response.flushBuffer();
            logger.debug("写入完成");
            if (request.getParameter("connection") == null || !request.getParameter("connection").equals("false")) {
                endpoint.registerToPoller(client, false, SelectionKey.OP_READ, nioSocketWrapper);
            }
            else
                nioSocketWrapper.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
