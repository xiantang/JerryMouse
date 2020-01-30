package info.xiantang.jerrymouse2.core.server;

import info.xiantang.jerrymouse2.core.handler.CountBaseHandler;
import info.xiantang.jerrymouses2.client.NetWorkClient;
import org.junit.Test;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static info.xiantang.jerrymouse2.core.server.Constants.CLOSED;
import static info.xiantang.jerrymouse2.core.server.Constants.SENDING;

public class MultiReactorTest {


//    public static class InsureReactorHandler extends CountBaseHandler {
//
//        public InsureReactorHandler(Selector selector, SocketChannel channel) throws IOException {
//            super(selector, channel);
//        }
//
//        @Override
//        public void process(ByteBuffer output) throws EOFException {
//            int state = getState();
//            if (state == CLOSED) {
//                throw new EOFException();
//            } else if (state == SENDING) {
//                output.clear();
//                String name = getReactorName();
//                output.put(name.getBytes());
//            }
//        }
//    }
//
//
//    @Test
//    public void canDealRequestWithSeveralReactor() throws IOException {
//        Reactor reactor = Reactor.newBuilder()
//                .setPort(9866)
//                .setHandlerClass(InsureReactorHandler.class)
//                .setMainReactorName("MainReactor")
//                .setSubReactorCount(3)
//                .build();
//        // start a new thread
//        reactor.start();
//
//        String first = NetWorkClient.doRequest("localhost", 9866, "test\n");
//        String second = NetWorkClient.doRequest("localhost", 9866, "test\n");
//        String third = NetWorkClient.doRequest("localhost", 9866, "test\n");
//        String nextRoundFirst = NetWorkClient.doRequest("localhost", 9866, "test\n");
//        List<String> responses = new ArrayList<>();
//        responses.add(first);
//        responses.add(second);
//        responses.add(third);
//        responses.add(nextRoundFirst);
//        Map<String, Long> result = responses.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
//        Map<String, Long> expect = new HashMap<>();
//        expect.put("SubReactor-1", 2L);
//        expect.put("SubReactor-2", 1L);
//        expect.put("SubReactor-3", 1L);
//        assert expect.equals(result);
//
//
//
//    }


}
