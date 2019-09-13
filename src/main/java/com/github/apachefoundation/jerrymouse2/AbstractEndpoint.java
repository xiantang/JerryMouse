package com.github.apachefoundation.jerrymouse2;

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

    protected abstract SocketChannel serverSocketAccept() throws Exception;

    public abstract void stopInternal();

    public abstract void bind() throws Exception;

    public Acceptor getAcceptor() {
        return acceptor;
    }

    public void resume() {
        if (running && paused) {
            paused = false;
        }
    }
}
