package com.github.apachefoundation.jerrymouse.network.connector.nio;

import com.github.apachefoundation.jerrymouse.network.endpoint.nio.NioEndpoint;
import com.github.apachefoundation.jerrymouse.network.wrapper.nio.NioSocketWrapper;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @Author: xiantang
 * @Date: 2019/4/17 14:45
 */
public class NioPoller implements Runnable {

    private NioEndpoint nioEndpoint;
    private Selector selector;
    private String pollerName;
    private NioWorker worker;
    private Logger logger = Logger.getLogger(NioPoller.class);
    /**
     *  事件队列
     */

    private Queue<PollerEvent> events;

    public NioPoller(NioEndpoint nioEndpoint, String pollerName) throws IOException {
        this.nioEndpoint = nioEndpoint;
        // 每个Poller 线程都对应了一个Selector
        this.selector = Selector.open();
        this.pollerName = pollerName;
        worker = new NioWorker();
        this.events = new ConcurrentLinkedDeque<>();
    }

    public Selector getSelector() {
        return selector;
    }


    public void register(SocketChannel socket, boolean isNewSocket, int eventType, NioSocketWrapper nioSocketWrapper) {
        if (nioSocketWrapper == null) {
            nioSocketWrapper = new NioSocketWrapper(nioEndpoint, this, socket, isNewSocket);
        }

        events.offer(new PollerEvent(nioSocketWrapper, eventType));
        // 如果selector 在select 阻塞 就调用wakeup立马返回
        // 通过调用wakeup() 使线程抛出ClosedSelectorException 提前返回
        selector.wakeup();
    }

    @Override
    public void run() {
        while (nioEndpoint.isRunning()) {
            try {
                events();
                // 若沒有事件就緒則不往下執行
                // TODO 改为同步阻塞方式
                int num = selector.selectNow();
                if (num <= 0) {
                    continue;
                }

                // 获得所有已就绪事件Key集合
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                for (Iterator<SelectionKey> it = selectedKeys.iterator(); it.hasNext(); ) {

                    SelectionKey key = it.next();
                    // 先判断是否有效
                    if (key.isValid()) {
                        if (key.isReadable()) {
                            key.cancel();
                            NioSocketWrapper socketWrapper = (NioSocketWrapper) key.attachment();
                            if (socketWrapper != null) {
                                worker.executeRead(socketWrapper);
                            }
                        } else if (key.isWritable()) {
                            logger.debug("管道可写");
                            key.cancel();
                            NioSocketWrapper socketWrapper = (NioSocketWrapper) key.attachment();
                            if (socketWrapper != null) {
                                worker.executeWrite(socketWrapper);
                            }
                        }
                    }
                    it.remove();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    private void events() {
        logger.debug("当前队列大小为 " + events.size());
        PollerEvent pollerEvent;
        for (int i = 0, size = events.size(); i < size && (pollerEvent = events.poll()) != null; i++) {
            new Thread(pollerEvent).start();

        }
    }


    private static class PollerEvent implements Runnable {
        private Logger logger = Logger.getLogger(PollerEvent.class);
        /**
         *  包装对象
         */
        private NioSocketWrapper wrapper;
        private int eventType;

        public PollerEvent(NioSocketWrapper wrapper, int eventType) {
            this.wrapper = wrapper;
            this.eventType = eventType;
        }

        public NioSocketWrapper getWrapper() {
            return wrapper;
        }


        @Override
        public void run() {
            logger.debug("将读事件注册到Poller的selector中");
            try {
                if (wrapper.getSocketChannel().isOpen()) {
                    if (eventType == SelectionKey.OP_READ) {
                        logger.debug("我注册了一个读事件");
                    } else if (eventType == SelectionKey.OP_WRITE) {
                        logger.debug("我注册了一个读事件");
                    } else {
                        logger.debug("我注册了一个其他事件");
                    }
                    wrapper.getSocketChannel().register(wrapper.getPoller().getSelector(), eventType, wrapper);
                } else {
                    logger.debug("socket已经关闭，无法注册到Poller");
                }
            } catch (ClosedChannelException e) {
                e.printStackTrace();
            }

        }


    }
}
