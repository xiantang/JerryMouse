package info.xiantang.jerrymouse2.core.handler;

import info.xiantang.jerrymouse2.core.reactor.MultiReactor;
import info.xiantang.jerrymouses2.client.EchoClient;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TestSampleEchoServer {
    @Test
    public void canCorrectAcceptCommend() throws IOException {
        MultiReactor reactor = MultiReactor.newBuilder()
                .setPort(9004)
                .setHandlerClass(EchoHandler.class)
                .setSubReactorCount(3)
                .build();

        Thread reactorT = new Thread(reactor);
        reactorT.start();

        EchoClient echoClient = new EchoClient("localhost", 9004);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            try {
                sb.append(echoClient.sendReceive("HELO" + i));
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        echoClient.sendReceive(""+(char) (3));
        assertEquals("HELO0reactor> HELO1reactor> HELO2", sb.toString());
    }
}
