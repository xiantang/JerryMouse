package info.xiantang.core.network.connector.nio;

import info.xiantang.core.context.WebApp;
import info.xiantang.core.handler.NioRequestHandler;
import info.xiantang.core.handler.NioResponseHandler;
import info.xiantang.core.network.wrapper.nio.NioSocketWrapper;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;

/*
 * work线程组
 */
public class NioWorker {
    /**
     * Read线程数量是cpu的核数 /2
     */
    private int ReadCount = Math.min(2, Runtime.getRuntime().availableProcessors())/2 + 1;

    /**
     * Write线程数量是cpu的核数 /2
     */
    private int WriteCount = Math.min(2, Runtime.getRuntime().availableProcessors())/4 + 1;
    //读 处理请求的线程池
    private ExecutorService ReadThreadPool;
    //写 线程池
    private ExecutorService WriteThreadPool;
    private  Logger logger = Logger.getLogger(WebApp.class);

    public NioWorker() {
        ThreadFactory ReadthreadFactory = new ThreadFactory() {
            private int count;
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "ReadWorker Pool-" + count++);
            }
        };

        ReadThreadPool = new ThreadPoolExecutor(200,
                200,
                1,
                TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                ReadthreadFactory);

        ThreadFactory WritethreadFactory = new ThreadFactory() {
            private int count;
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "WriteWorker Pool-" + count++);
            }
        };

        WriteThreadPool = new ThreadPoolExecutor(200,
                200,
                1,
                TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                WritethreadFactory);
    }

    public void executeRead(NioSocketWrapper nioSocketWrapper) {
        try {
//            System.out.println("注册读");
            ReadThreadPool.execute(new NioRequestHandler(nioSocketWrapper));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void executeWrite(NioSocketWrapper nioSocketWrapper) {
        try {
            logger.info("注册写");
            WriteThreadPool.execute(new NioResponseHandler(nioSocketWrapper));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
