package info.xiantang.core.network.dispatcher;

import info.xiantang.core.network.wrapper.SocketWrapper;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/*
 * 请求分发器
 * 接收到传递过来的请求(socket 事件) 分发给 servlet
 */
public abstract class AbstractDispatcher {

    protected ThreadPoolExecutor pool;

    public AbstractDispatcher() {
        ThreadFactory threadFactory = new ThreadFactory() {
            private int count;

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "Worker Pool-" + count++);
            }
        };
        this.pool = new ThreadPoolExecutor(100,100, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(200));

    }
    public void shutdown() {
        pool.shutdown();

    }

    public abstract void doDispatch(SocketWrapper client) throws IOException;
}
