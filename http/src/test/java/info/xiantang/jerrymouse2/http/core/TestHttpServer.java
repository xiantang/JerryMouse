package info.xiantang.jerrymouse2.http.core;

import info.xiantang.jerrymouse2.core.servlet.Servlet;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TestHttpServer {

    @Test
    public void testHttpServerCanHandleCertainServlet() throws IOException {
        Map<String, Servlet> mapper = new HashMap<>();
        Servlet servlet = (request, response) -> response.setBody("test\ntest");
        mapper.put("/test",servlet);
        HttpServer server = new HttpServer(8080,mapper);
        server.start();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://localhost:8080/test");
        CloseableHttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        String actual = EntityUtils.toString(entity);
        assertEquals("test\ntest", actual);

    }


}
