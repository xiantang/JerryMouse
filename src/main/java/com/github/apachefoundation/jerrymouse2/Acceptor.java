package com.github.apachefoundation.jerrymouse2;

import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

/**
 * 设计理念
 * 1. 使用阻塞的方式阻塞的接受线程
 * 2. 设置能够恢复的暂停策略
 *  当调用 pause()方法的时候并不代表
 *  服务就停止了，Acceptor 会进入空转
 *  直到 resume()方法被调用
 *
 * @Author: xiantang
 * @Date: 2019/9/9 20:52
 */
public class Acceptor implements Runnable {

    private AbstractEndpoint endpoint;

    Acceptor(AbstractEndpoint endpoint) {
        this.endpoint = endpoint;
    }

    private volatile AcceptorState state = AcceptorState.NEW;

    @Override
    public void run() {
        while (endpoint.isRunning()) {

            while (endpoint.isRunning() && endpoint.isPaused()) {
                state = AcceptorState.PAUSED;
                try {
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException e) {
                    // 忽略中断
                }
            }

            state = AcceptorState.RUNNING;
            SocketChannel socket = null;
            try {
                endpoint.countUpOrAwaitConnection();
                try {
                    // 接受下一个从server 来临的连接
                    socket = endpoint.serverSocketAccept();
                    System.out.println("accepted");
                } catch (Exception e) {
                    // countDown
                    e.printStackTrace();
                    endpoint.countDownConnection();
                }
                if (endpoint.isRunning() && !endpoint.isPaused()) {
                    if (!endpoint.setSocketOptions(socket)) {
                        // TODO destroy socket
                    }
                }
            } catch (InterruptedException e) {
                // TODO 检查错误
                e.printStackTrace();
            }

        }
        state = AcceptorState.ENDED;
    }

    public AcceptorState getState() {
        return state;
    }

    public enum AcceptorState {
        NEW, RUNNING, PAUSED, ENDED
    }
}
