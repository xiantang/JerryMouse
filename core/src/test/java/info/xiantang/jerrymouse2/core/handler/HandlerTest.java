package info.xiantang.jerrymouse2.core.handler;

import info.xiantang.jerrymouse2.core.server.MultiReactor;
import info.xiantang.jerrymouse2.core.server.MultiReactorTest;
import info.xiantang.jerrymouse2.core.server.Reactor;
import info.xiantang.jerrymouses2.client.NetWorkClient;
import org.junit.Test;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.*;

import static info.xiantang.jerrymouse2.core.server.Constants.*;


public class HandlerTest {

    private ExecutorService executor = Executors.newFixedThreadPool(4);


    public static class SleepyHandler extends CountBaseHandler {

        public SleepyHandler(Reactor reactor, SocketChannel channel) throws IOException {
            super(reactor, channel);
        }

        @Override
        public void process(ByteBuffer output) throws EOFException {
            int state = getState();
            if (state == CLOSED) {
                throw new EOFException();
            } else if (state == SENDING) {
                output.clear();
                output.put(Thread.currentThread().getName().getBytes());
            }
        }
    }

    @Test
    public void isHandlerConcurrency() throws IOException {
        MultiReactor reactor = MultiReactor.newBuilder()
                .setPort(9800)
                .setHandlerClass(SleepyHandler.class)
                .setSubReactorCount(3)
                .build();

        Set<String> result = new HashSet<>();
        Thread reactorT = new Thread(reactor);
        reactorT.start();

        List<Future<String>> futureList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Future<String> future = executor.submit(() -> NetWorkClient.doRequest("localhost", 9800, "test\n"));
            futureList.add(future);
        }
        futureList.forEach(x -> {
            try {
                String s = x.get();
                System.out.println(s);
                assert s.startsWith("pool-");
                result.add(s);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });


        assert result.size() > 1;
    }

    @Test
    public void sampleBaseHandlerCanReturnSameResult() throws IOException {
        MultiReactor reactor = MultiReactor.newBuilder()
                .setPort(9801)
                .setHandlerClass(MultiReactorTest.InsureReactorHandler.class)
                .setMainReactorName("MainReactor")
                .setSubReactorCount(3)
                .build();
        Thread reactorT = new Thread(reactor);
        reactorT.start();
        String response = NetWorkClient.doRequest("localhost", 9801, "test\n");
        System.out.println(response);
        assert "subReactor-2".equals(response);

    }


}
