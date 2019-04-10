package info.xiantang.core.network.dispatcher;

import info.xiantang.core.network.wrapper.SocketWrapper;

import java.io.IOException;


import java.util.concurrent.*;

/*
 * 请求分发器
 * 接收到传递过来的请求(socket 事件) 分发给 servlet
 */




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


        /*
           關於CallerRunsPolicy
           中止(Abort)是默認的飽和策略 將會抛出RejectedExecutionException
           CallerRunsPolicy 則是將綫程交給調用者去運行
           使用的是run方法 不是創建綫程的方式
         */
        this.pool = new ThreadPoolExecutor(100,
                100,
                                        1,
                                        TimeUnit.SECONDS,
                                        new ArrayBlockingQueue<Runnable>(200),
                                        threadFactory,
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    /**`
     * 支持关闭功能
     */

    public void shutdown() {
        pool.shutdown();

    }

    public abstract void doDispatch(SocketWrapper client) throws IOException;
}
