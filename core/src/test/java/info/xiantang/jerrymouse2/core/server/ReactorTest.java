package info.xiantang.jerrymouse2.core.server;

import info.xiantang.jerrymouse2.core.handler.CountBaseHandler;
import info.xiantang.jerrymouses2.client.NetWorkClient;
import org.junit.Test;

import java.io.IOException;


public class ReactorTest {


    @Test
    public void canAcceptRequests() throws IOException {
        MultiReactor reactor = MultiReactor.newBuilder()
                .setPort(9865)
                .setHandlerClass(CountBaseHandler.class)
                .setSubReactorCount(3)
                .build();
        Thread reactorT = new Thread(reactor);
        reactorT.start();

        String response = NetWorkClient.doRequest("localhost",9865,"test\n");
        assert "1".equals(response);

    }
}
