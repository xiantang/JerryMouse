package info.xiantang.basic.core.network.connector.nio;

import info.xiantang.basic.core.network.endpoint.nio.NioEndpoint;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class NioPoller implements Runnable {

    private NioEndpoint nioEndpoint;
    private Selector selector;
    private String pollerName;
    private Queue<SocketChannel> events;

    public NioPoller(NioEndpoint nioEndpoint, String pollerName) throws IOException {
        this.nioEndpoint = nioEndpoint;
        this.selector = Selector.open();
        this.pollerName = pollerName;
        this.events = new ConcurrentLinkedDeque<>();
    }

    public void register(SocketChannel socket) {
        try {

            socket.register(selector, SelectionKey.OP_READ);
            System.out.println("注册成功");

        } catch (ClosedChannelException e) {
            e.printStackTrace();
        }
        events.offer(socket);
        // 如果selector 在select 阻塞 就调用wakeup立马返回
        selector.wakeup();
    }

    @Override
    public void run() {
        while (nioEndpoint.isRunning()) {

            try {

                int num = selector.select();
                if (num <= 0) {
                    continue;
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


}
