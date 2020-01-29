package info.xiantang.jerrymouse2.core.server;

import info.xiantang.jerrymouses2.client.NetWorkClient;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;

import java.io.EOFException;
import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static info.xiantang.jerrymouse2.core.server.Constants.*;


public class HandlerTest {

    private ExecutorService executor = Executors.newFixedThreadPool(2);


    static class SleepyHandler extends CountBaseHandler {

        public SleepyHandler(Selector sel, SocketChannel c) throws IOException {
            super(sel, c);
        }

        public void process() throws EOFException {
            if (state == CLOSED) {
                throw new EOFException();
            } else if (state == SENDING) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                output.clear();
                output.put("12".getBytes());
            }
        }
    }

    @Test
    public void isHandlerConcurrency() throws IOException {
        Reactor reactor = Reactor.newBuilder()
                .setPort(9800)
                .setHandlerClass(SleepyHandler.class)
                .build();
        Thread reactorT = new Thread(reactor);
        reactorT.start();
        StopWatch watch = new StopWatch();
        watch.start();
        List<Future<String>> futureList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Future<String> future = executor.submit(() -> NetWorkClient.doRequest("localhost", 9800, "test\n"));
            futureList.add(future);
        }
        futureList.forEach(x -> {
            try {
                String s = x.get();
                assert "12".equals(s);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });


        watch.stop();
        long spend = watch.getTime();
        System.out.println(spend);
        assert spend < 2000 && spend >=1000;
    }

    @Test
    public void sampleBaseHandlerCanReturnSameResult() throws IOException {
        Reactor reactor = Reactor.newBuilder()
                .setPort(9801)
                .setHandlerClass(SampleBaseHandler.class)
                .build();
        Thread reactorT = new Thread(reactor);
        reactorT.start();
        String response = NetWorkClient.doRequest("localhost", 9801, "test\n");
        assert "test".equals(response);

    }


}
