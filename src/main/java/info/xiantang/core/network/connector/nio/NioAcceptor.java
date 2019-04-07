package info.xiantang.core.network.connector.nio;

import info.xiantang.core.network.endpoint.nio.NioEndpoint;

import java.io.IOException;
import java.nio.channels.SocketChannel;

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
                client = nioEndpoint.accept();
                if (client == null) {
                    continue;
                }
                // 必须设置通道为非阻塞才能向select注册
                client.configureBlocking(false);
                nioEndpoint.registerToPoller(client,true);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
