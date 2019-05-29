package com.github.apachefoundation.jerrymouse.network.connector.nio;

import com.github.apachefoundation.jerrymouse.network.endpoint.nio.NioEndpoint;
import com.github.apachefoundation.jerrymouse.network.wrapper.nio.NioSocketWrapper;
import com.github.apachefoundation.jerrymouse.context.WebApp;
import com.github.apachefoundation.jerrymouse.exception.handler.ExceptionHandler;
import com.github.apachefoundation.jerrymouse.network.wrapper.SocketWrapper;
import com.github.apachefoundation.jerrymouse.processor.HttpProcessor;
import org.apache.log4j.Logger;

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
            readThreadPool.execute(new Reader(nioSocketWrapper));
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

        private NioSocketWrapper nioSocketWrapper;

        public Reader(SocketWrapper socketWrapper) throws IOException {
            this.nioSocketWrapper = (NioSocketWrapper)socketWrapper;
        }

        @Override
        public void run() {
            SocketChannel socketChannel = nioSocketWrapper.getSocketChannel();
            HttpProcessor httpProcessor = new HttpProcessor();
            httpProcessor.process(socketChannel, nioSocketWrapper);
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
                // 判断是否keep-alive
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
