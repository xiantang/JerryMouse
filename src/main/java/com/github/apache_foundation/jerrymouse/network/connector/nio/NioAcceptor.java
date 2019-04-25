package com.github.apache_foundation.jerrymouse.network.connector.nio;

import com.github.apache_foundation.jerrymouse.network.endpoint.nio.NioEndpoint;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
/**
 * @Author: xiantang
 * @Date: 2019/4/17 14:45
 */
public class NioAcceptor implements Runnable {
    private NioEndpoint nioEndpoint;

    public NioAcceptor(NioEndpoint nioEndpoint) {
        this.nioEndpoint = nioEndpoint;
    }

    @Override
    public void run() {

        while (nioEndpoint.isRunning()) {

            SocketChannel client;
            try {

                // 若沒有事件就緒則不往下執行

                client = nioEndpoint.accept();
                if (client == null) {
                    continue;
                }
                // 必须设置通道为非阻塞才能向select注册
                client.configureBlocking(false);

                // 注册到Poller 中去

                nioEndpoint.registerToPoller(client,true, SelectionKey.OP_READ, null);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
