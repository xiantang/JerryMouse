package info.xiantang.jerrymouse2.core.handler;

import info.xiantang.jerrymouse2.core.reactor.Constants;
import info.xiantang.jerrymouse2.core.reactor.MultiReactor;
import info.xiantang.jerrymouse2.core.reactor.MultiReactorTest;
import info.xiantang.jerrymouse2.core.reactor.Reactor;
import info.xiantang.jerrymouses2.client.NetWorkClient;
import org.junit.Test;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static info.xiantang.jerrymouse2.core.reactor.Constants.CLOSED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class HandlerTest {

    private ExecutorService executor = Executors.newFixedThreadPool(4);

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
            Future<String> future = executor.submit(()
                    -> NetWorkClient.doRequest("localhost", 9800, "test\n"));
            futureList.add(future);
        }
        futureList.forEach(x -> {
            try {
                String s = x.get();
                System.out.println(s);
                assertTrue(s.startsWith("pool-"));
                result.add(s);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });


        assertTrue(result.size() > 1);
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
        assertEquals("subReactor-2", response);

    }

    public static class SleepyHandler extends CountBaseHandler {

        public SleepyHandler(Reactor reactor, SocketChannel channel) throws IOException {
            super(reactor, channel);
        }

        @Override
        public void process(ByteBuffer output,StringBuilder request) throws EOFException {
            int state = getState();
            if (state == CLOSED) {
                throw new EOFException();
            } else if (state == Constants.SENDING) {
                output.clear();
                output.put(Thread.currentThread().getName().getBytes());
            }
        }
    }


}
