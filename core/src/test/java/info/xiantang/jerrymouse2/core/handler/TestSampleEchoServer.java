package info.xiantang.jerrymouse2.core.handler;

import info.xiantang.jerrymouse2.core.server.Reactor;
import info.xiantang.jerrymouses2.client.EchoClient;
import org.junit.Test;

import java.io.IOException;

public class TestSampleEchoServer {
    @Test
    public void canCorrectAcceptCommend() throws IOException {
        Reactor reactor = Reactor.newBuilder()
                .setPort(9004)
                .setHandlerClass(EchoHandler.class)
                .build();
        Thread reactorT = new Thread(reactor);
        reactorT.start();

        EchoClient echoClient = new EchoClient("localhost", 9004);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            try  {
                sb.append(echoClient.sendReceive("HELO" + i));
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(sb.toString());
        assert "HELO0reactor> HELO1reactor> HELO2".equals(sb.toString());
        
    }
}
