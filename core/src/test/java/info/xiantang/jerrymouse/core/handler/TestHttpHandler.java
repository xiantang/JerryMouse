package info.xiantang.jerrymouse.core.handler;

import info.xiantang.jerrymouse.core.reactor.MultiReactor;
import info.xiantang.jerrymouse.core.server.ServletWrapper;
import info.xiantang.jerrymouse.core.utils.NetUtils;
import info.xiantang.jerrymouse.http.servlet.Request;
import info.xiantang.jerrymouse.http.servlet.Response;
import info.xiantang.jerrymouse.http.servlet.Servlet;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class TestHttpHandler {

    static class RequestServlet implements Servlet {

        @Override
        public void service(Request request, Response response) throws IOException {
            OutputStream responseOutputStream = response.getResponseOutputStream();
            String s = "GET / HTTP/1.1\r\n" +
                    "Host: localhost:9820\r\n" +
                    "Connection: Keep-Alive\r\n" +
                    "User-Agent: Apache-HttpClient/4.5.3 (Java/1.8.0_232)\r\n" +
                    "Accept-Encoding: gzip,deflate\r\n" +
                    "\r\n";
            responseOutputStream.write(s.getBytes());
        }
    }


    @Test
    public void httpHandlerCanReturnRequestHeaderAndBody() throws IOException {
        int availablePort = NetUtils.getAvailablePort();

        HashMap<String, ServletWrapper> mapper = new HashMap<>();
        ServletWrapper empty = new ServletWrapper(
                "empty",
                "/",
                "xxx.xxxx.xxxxx.IndexServlet",
                0,
                RequestServlet.class,
                new RequestServlet()
        );
        mapper.put("/", empty);
        MultiReactor reactor = MultiReactor.newBuilder()
                .setPort(availablePort)
                .setHandlerClass(HttpHandler.class)
                .setServletContext(new ServletContext(null, null, mapper, null, "sample.jar"))
                .setSubReactorCount(3)
                .build();
        Thread reactorT = new Thread(reactor);
        reactorT.start();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://localhost:" + availablePort);
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
