package info.xiantang.core.network.dispatcher;

import info.xiantang.core.network.wrapper.SocketWrapper;

import java.io.IOException;


import java.util.concurrent.*;

/*
 * 请求分发器
 * 接收到传递过来的请求(socket 事件) 分发给 servlet
 */



@Deprecated
public abstract class AbstractDispatcher {

    protected ThreadPoolExecutor pool;

    public AbstractDispatcher() {
        // 设置线程工厂类
        ThreadFactory threadFactory = new ThreadFactory() {
            private int count;
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "Worker Pool-" + count++);
            }
        };


        this.pool = new ThreadPoolExecutor(200,
                200,
                                        1,
                                        TimeUnit.SECONDS,
                                        new SynchronousQueue<Runnable>(),
                                        threadFactory);
    }

    /**`
     * 支持关闭功能
     */

    public void shutdown() {
        pool.shutdown();

    }

    public abstract void doDispatch(SocketWrapper client) throws IOException;
}
