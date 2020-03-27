package info.xiantang.jerrymouse.core.reactor;

import info.xiantang.jerrymouse.core.handler.CountBaseHandler;
import info.xiantang.jerrymouse.core.handler.HandlerContext;
import info.xiantang.jerrymouse.core.utils.NetUtils;
import info.xiantang.jerrymouses2.client.NetWorkClient;
import org.apache.http.util.ByteArrayBuffer;
import org.junit.Test;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static info.xiantang.jerrymouse.core.reactor.Constants.CLOSED;
import static info.xiantang.jerrymouse.core.reactor.Constants.SENDING;
import static org.junit.Assert.assertEquals;

public class MultiReactorTest {


    @Test
    public void canDealRequestWithSeveralReactor() throws IOException {
        int availablePort = NetUtils.getAvailablePort();
        MultiReactor reactor = MultiReactor.newBuilder()
                .setPort(availablePort)
                .setHandlerClass(InsureReactorHandler.class)
                .setMainReactorName("MainReactor")
                .setSubReactorCount(3)
                .build();
        // start a new thread
        Thread reactorT = new Thread(reactor);
        reactorT.start();
        String first = NetWorkClient.doRequest("localhost", availablePort, "test\n");
        String second = NetWorkClient.doRequest("localhost", availablePort, "test\n");
        String third = NetWorkClient.doRequest("localhost", availablePort, "test\n");
        String nextRoundFirst = NetWorkClient.doRequest("localhost", availablePort, "test\n");
        List<String> responses = new ArrayList<>();
        responses.add(first);
        responses.add(second);
        responses.add(third);
        responses.add(nextRoundFirst);
        Map<String, Long> result = responses.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        Map<String, Long> expect = new HashMap<>();
        expect.put("subReactor-0", 1L);
        expect.put("subReactor-1", 1L);
        expect.put("subReactor-2", 2L);
        assertEquals(expect, result);
    }

    public static class InsureReactorHandler extends CountBaseHandler {

        public InsureReactorHandler(HandlerContext context) {
            super(context);
        }

        @Override
        public void process(ByteBuffer output, ByteArrayBuffer request) throws EOFException {
            int state = getState();
            if (state == CLOSED) {
                throw new EOFException();
            } else if (state == SENDING) {
                output.clear();
                String name = getReactorName();
                output.put(name.getBytes());
            }
        }
    }


}
