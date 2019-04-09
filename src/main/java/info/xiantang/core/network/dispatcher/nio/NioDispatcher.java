package info.xiantang.core.network.dispatcher.nio;

import info.xiantang.core.handler.NioRequestHandler;

import info.xiantang.core.http.HttpRequest;
import info.xiantang.core.http.HttpResponse;
import info.xiantang.core.network.dispatcher.AbstractDispatcher;
import info.xiantang.core.network.wrapper.SocketWrapper;
import info.xiantang.core.network.wrapper.nio.NioSocketWrapper;

import java.io.IOException;



import java.nio.channels.SocketChannel;

/**
 * 分发器
 * 获得请求的url并且分配给指定的servlet处理
 */

public class NioDispatcher extends AbstractDispatcher {
    @Override
    public void doDispatch(SocketWrapper socketWrapper) throws IOException {
        NioSocketWrapper nioSocketWrapper = (NioSocketWrapper) socketWrapper;

        //从这里开始改
        SocketChannel socketChannel = nioSocketWrapper.getSocketChannel();
        HttpRequest httpRequest = new HttpRequest(socketChannel);
        HttpResponse httpResponse = new HttpResponse(socketChannel);

        try {
            pool.execute(new NioRequestHandler(nioSocketWrapper,httpRequest,httpResponse));
        } catch (IOException e) {
            e.printStackTrace();
            nioSocketWrapper.close();
        }
    }

    }

