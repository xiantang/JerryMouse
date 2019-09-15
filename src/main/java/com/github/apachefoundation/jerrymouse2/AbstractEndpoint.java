package com.github.apachefoundation.jerrymouse2;

import com.github.apachefoundation.jerrymouse2.util.LimitLatch;

import java.nio.channels.SocketChannel;

/**
 * @Author: xiantang
 * @Date: 2019/9/9 20:35
 */
public abstract class AbstractEndpoint {
    /**
     * Running state of the endpoint.
     */
    volatile boolean running = false;

    Acceptor acceptor;

    boolean paused = false;

    private volatile LimitLatch connectionLimitLatch = null;

    private int maxConnections = 1000;

    public boolean isPaused() {
        return paused;
    }

    public boolean isRunning(){
        return running;
    }

    public abstract void startInternal();

    public void pause() {
        if (running && !paused) {
            paused = true;
        }
    }

    /**
     * 获得最大连接
     * @return
     */
    public int getMaxConnections() {
        return maxConnections;
    }

    protected abstract SocketChannel serverSocketAccept() throws Exception;

    public abstract void stopInternal();

    public abstract void bind(int port) throws Exception;

    public Acceptor getAcceptor() {
        return acceptor;
    }

    public void resume() {
        if (running && paused) {
            paused = false;
        }
    }

    public void setMaxConnections(int maxCon) {
        this.maxConnections = maxCon;
        LimitLatch latch = this.connectionLimitLatch;
        if (latch != null) {
            latch.setLimit(maxCon);
        } else if (maxCon > 0) {
            initializeConnectionLatch();
        }
    }

    private LimitLatch initializeConnectionLatch() {
        if (connectionLimitLatch == null) {
            connectionLimitLatch = new LimitLatch(getMaxConnections());
        }
        return connectionLimitLatch;
    }

    public abstract void unbind() throws Exception;

    protected void countUpOrAwaitConnection() throws InterruptedException {
        LimitLatch latch = connectionLimitLatch;
        if (latch != null) {
            latch.countUpOrAwait();
        }

    }

    public void countDownConnection() {
        LimitLatch latch = connectionLimitLatch;
        if (latch != null) {
            latch.countDown();
        }
    }

    public abstract boolean setSocketOptions(SocketChannel socket);
}
