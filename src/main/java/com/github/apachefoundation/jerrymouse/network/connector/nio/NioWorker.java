package com.github.apachefoundation.jerrymouse.network.connector.nio;

import com.github.apachefoundation.jerrymouse.http.HttpResponse;
import com.github.apachefoundation.jerrymouse.network.wrapper.nio.NioSocketWrapper;
import com.github.apachefoundation.jerrymouse.network.wrapper.SocketWrapper;
import com.github.apachefoundation.jerrymouse.processor.HttpProcessor;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.*;

import static com.github.apachefoundation.jerrymouse.network.endpoint.nio.NioEndpoint.CORE_NUM;

/**
 * @Author: xiantang
 * @Date: 2019/4/17 14:45
 */
class NioWorker {

    /**
     * 读处理请求的线程池
     */
    private ExecutorService worker;
    /**
     * 写 线程池
     */
    private  Logger logger = Logger.getLogger(NioWorker.class);

    /**
     * 初始化Readers
     */
    private void init() {
        worker = new ThreadPoolExecutor(CORE_NUM*2,
                CORE_NUM*2,
                1000,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>());
    }


    NioWorker() {
        init();
    }

    void executeRead(NioSocketWrapper nioSocketWrapper) {
        worker.execute(new Reader(nioSocketWrapper));
    }

    void executeWrite(NioSocketWrapper nioSocketWrapper) {
        logger.debug("执行写");
        worker.execute(new Writer(nioSocketWrapper));
    }


    public class Reader implements Runnable {

        private NioSocketWrapper nioSocketWrapper;

        Reader(SocketWrapper socketWrapper) {
            this.nioSocketWrapper = (NioSocketWrapper)socketWrapper;
        }

        @Override
        public void run() {
            logger.debug("执行读");
            SocketChannel socketChannel = nioSocketWrapper.getSocketChannel();
            HttpProcessor httpProcessor = new HttpProcessor();
            httpProcessor.process(socketChannel, nioSocketWrapper);
        }

    }

    public class Writer implements Runnable {

        private NioSocketWrapper nioSocketWrapper;
        private  Logger logger = Logger.getLogger(Writer.class);
        Writer(SocketWrapper socketWrapper) {
            this.nioSocketWrapper = (NioSocketWrapper)socketWrapper;
        }

        @Override
        public void run() {
            HttpResponse response = nioSocketWrapper.getResponse();
            try {
                response.flushBuffer();
                logger.debug("写入完成");
                // TODO 这里有个长连接的BUG 暂时去除重新注册的功能
                nioSocketWrapper.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
