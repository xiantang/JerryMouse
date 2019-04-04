package info.xiantang.basic.core.network.connector.nio;

import info.xiantang.basic.core.network.endpoint.nio.NioEndpoint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
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
            socket.register(selector, SelectionKey.OP_READ,socket);
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
                for (Iterator<SelectionKey> it =selector.selectedKeys().iterator();it.hasNext();) {
                    SelectionKey key = it.next();
                    if (key.isReadable()) {
                        // 读取事件就绪
                        SocketChannel socket = (SocketChannel)key.attachment();
                        if (socket != null) {
                            processSocket(socket);
                        }
                        it.remove();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 將SocketChannel 交付給綫程池進行處理
     * @param socket
     * @throws IOException
     */
    private void processSocket(SocketChannel socket) throws IOException {

        nioEndpoint.execute(socket);


    }


}
