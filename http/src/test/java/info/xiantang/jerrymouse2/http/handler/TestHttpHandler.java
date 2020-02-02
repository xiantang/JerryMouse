package info.xiantang.jerrymouse2.http.handler;

import info.xiantang.jerrymouse2.core.reactor.MultiReactor;
import info.xiantang.jerrymouse2.http.handler.HttpHandler;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TestHttpHandler {


    @Test
    public void httpHandlerCanReturnRequestHeaderAndBody() throws IOException {
        MultiReactor reactor = MultiReactor.newBuilder()
                .setPort(9820)
                .setHandlerClass(HttpHandler.class)
                .setSubReactorCount(3)
                .build();
        Thread reactorT = new Thread(reactor);
        reactorT.start();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://localhost:9820");
        CloseableHttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        String responseStr = EntityUtils.toString(entity);
        String expect = "GET / HTTP/1.1\r\n" +
                "Host: localhost:9820\r\n" +
                "Connection: Keep-Alive\r\n" +
                "User-Agent: Apache-HttpClient/4.5.3 (Java/1.8.0_232)\r\n" +
                "Accept-Encoding: gzip,deflate\r\n" +
                "\r\n";
        assertEquals(expect, responseStr);
    }


}
