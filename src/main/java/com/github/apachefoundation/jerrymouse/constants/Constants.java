package com.github.apachefoundation.jerrymouse.constants;

/**
 * @Author: xiantang
 * @Date: 2019/5/21 23:59
 */
public class Constants {
    public static final String CONTENT_TYPE = "contenttype";
    public static final String CONTENT_LENGTH = "contentlength";
    public static final String CONNECTION = "connection";
    public static final String KEEPALIVE = "keep-alive";
    public static final String COOKIE = "cookie";
    public static final String WEB_ROOT = System.getProperty("user.dir")+"\\src\\main\\resource";
    public static final String HOST = "host";
    /**
     * Poller线程数量是cpu的核数 参考tomcat
     * 对于计算密集性的任务 当线程池的大小为 N cpu+1 通常能实现最优的利用率
     * (当计算密集型的线程偶尔由于页缺失或者其他情况而暂停的时候
     * ，这个额外的线程可以CPU时钟周期不会被浪费)
     * *********************新加注释**********************
     * poller 线程池用于监听 socket 事件，开销应比 work 线程要小，故分配 1/4 的线程数量
     * 加一是因为我电脑没那么多核 搞成 0 个线程了都
     */
    public static final int  POLLER_COUNT = Math.min(2, Runtime.getRuntime().availableProcessors()) / 4;
}
