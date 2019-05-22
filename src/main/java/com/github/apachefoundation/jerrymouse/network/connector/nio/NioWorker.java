package com.github.apachefoundation.jerrymouse.network.connector.nio;

import com.github.apachefoundation.jerrymouse.http.HttpRequest;
import com.github.apachefoundation.jerrymouse.network.endpoint.nio.NioEndpoint;
import com.github.apachefoundation.jerrymouse.network.wrapper.nio.NioSocketWrapper;
import com.github.apachefoundation.jerrymouse.context.WebApp;
import com.github.apachefoundation.jerrymouse.exception.handler.ExceptionHandler;
import com.github.apachefoundation.jerrymouse.http.HttpResponse;
import com.github.apachefoundation.jerrymouse.network.wrapper.SocketWrapper;
import com.github.apachefoundation.jerrymouse.utils.SocketInputStream;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.*;

/**
 * @Author: xiantang
 * @Date: 2019/4/17 14:45
 */
public class NioWorker {

    /**
     * 读 处理请求的线程池
     */
    private ExecutorService readThreadPool;
    /**
     * 写 线程池
     */
    private ExecutorService writeThreadPool;
    private  Logger logger = Logger.getLogger(WebApp.class);
    private ExceptionHandler exceptionHandler;


    /**
     * 初始化Readers
     */
    private void initReader() {
        ThreadFactory readThreadFactory = new ThreadFactory() {
            private int count;
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "ReadWorker Pool-" + count++);
            }
        };
        readThreadPool = new ThreadPoolExecutor(200,
                200,
                1,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                readThreadFactory);
    }
    /**
     * 初始化Writer
     */
    private void initWriter() {
        ThreadFactory writeThreadFactory = new ThreadFactory() {
            private int count;
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "WriteWorker Pool-" + count++);
            }
        };

        writeThreadPool = new ThreadPoolExecutor(200,
                200,
                1,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                writeThreadFactory);
    }

    public NioWorker() {
        this.exceptionHandler = new ExceptionHandler();
        initReader();
        initWriter();
    }

    public void executeRead(NioSocketWrapper nioSocketWrapper) {
        try {
            readThreadPool.execute(new Reader(nioSocketWrapper,exceptionHandler));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void executeWrite(NioSocketWrapper nioSocketWrapper) {
        try {
            logger.debug("注册写");
            writeThreadPool.execute(new Writer(nioSocketWrapper,exceptionHandler));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public class Reader implements Runnable {

        private HttpServlet servlet;
        private SocketChannel client;
        private NioEndpoint endpoint;
        private NioSocketWrapper nioSocketWrapper;
        private ExceptionHandler exceptionHandler;
        private Logger logger = Logger.getLogger(Reader.class);

        public Reader(SocketWrapper socketWrapper, ExceptionHandler exceptionHandler) throws IOException {
            this.nioSocketWrapper = (NioSocketWrapper)socketWrapper;
            this.exceptionHandler = exceptionHandler;
            SocketChannel socketChannel = nioSocketWrapper.getSocketChannel();
            this.endpoint =nioSocketWrapper.getServer();
            this.client = nioSocketWrapper.getSocketChannel();
        }

        @Override
        public void run() {
            try {

                SocketChannel socketChannel = nioSocketWrapper.getSocketChannel();
                SocketInputStream requestSocketInputStream = new SocketInputStream(socketChannel);
                HttpServletRequest request = new HttpRequest(requestSocketInputStream);
                HttpServletResponse response = new HttpResponse(socketChannel);
                ((HttpResponse) response).setRequest(request);
                nioSocketWrapper.setResponse(response);
                nioSocketWrapper.setRequest(request);
                // TODO 静态资源支持
                if (request.getRequestURI().endsWith("html") ||
                        request.getRequestURI().endsWith("js") ||
                        request.getRequestURI().endsWith("png") ||
                    request.getRequestURI().endsWith("css")) {
                    socketChannel.configureBlocking(true);
                    ((HttpResponse) response).sendStaticResource();
                    socketChannel.configureBlocking(false);
                } else {
                    servlet = (HttpServlet) WebApp.getServletFromUrl(request.getRequestURI());
                    if (servlet != null) {
                        servlet.service(request, response);
                    } else {
                        servlet = (HttpServlet) WebApp.getServletFromUrl("404");
                        servlet.service(request, response);
                    }
                }
                logger.debug("开始注册写事件");
                endpoint.registerToPoller(client, false, SelectionKey.OP_WRITE, nioSocketWrapper);
                logger.debug("写事件完成注册");
                //关闭之后才可以完全奖body写入
                response.getWriter().close();

            } catch (IOException e) {
                exceptionHandler.handle(e, nioSocketWrapper);
            } catch (ServletException e) {
                exceptionHandler.handle(e, nioSocketWrapper);
            }



        }
    }

    public class Writer implements Runnable {

        private SocketChannel client;
        private NioEndpoint endpoint;
        private NioSocketWrapper nioSocketWrapper;
        private  Logger logger = Logger.getLogger(Writer.class);
        public Writer(SocketWrapper socketWrapper, ExceptionHandler exceptionHandler) throws IOException {
            this.nioSocketWrapper = (NioSocketWrapper)socketWrapper;
            this.endpoint =(NioEndpoint) nioSocketWrapper.getServer();
            this.client = nioSocketWrapper.getSocketChannel();
        }

        @Override
        public void run() {
            HttpServletResponse response = nioSocketWrapper.getResponse();
            HttpServletRequest request = nioSocketWrapper.getRequest();
            String connection = "connection";
            try {
                response.flushBuffer();
                logger.debug("写入完成");
                if (request.getParameter(connection) == null || !request.getParameter("connection").equals("false")) {
                    endpoint.registerToPoller(client, false, SelectionKey.OP_READ, nioSocketWrapper);
                } else {
                    nioSocketWrapper.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
