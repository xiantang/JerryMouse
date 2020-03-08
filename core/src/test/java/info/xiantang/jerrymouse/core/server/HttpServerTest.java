package info.xiantang.jerrymouse.core.server;

import info.xiantang.jerrymouse.core.conf.Configuration;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class HttpServerTest {

    @Test
    public void httpServerCanLoadJarPackage() throws IOException {
        HttpServer httpSever = new HttpServer();
        httpSever.loadSources();
        List<ServerSource> sources = httpSever.getServerSources();
        Map<String, ServletWrapper> router = new HashMap<>();
        ServletWrapper wrapper1 = new ServletWrapper(
                "index",
                "/",
                "info.xiantang.jerrymouse.sample.IndexServlet",
                0,
                null,
                null);
        ServletWrapper wrapper2 = new ServletWrapper(
                "hello",
                "/hello",
                "info.xiantang.jerrymouse.sample.HelloServlet",
                null,
                null,
                null);
        router.put("/", wrapper1);
        router.put("/hello", wrapper2);
        List<ServerSource> expect = new ArrayList<>();
        Configuration config = new Configuration(9000, 3, router);
        expect.add(new ServerSource("sample.jar",config));
        assertEquals(expect, sources);
    }


    @Test
    public void httpServerCanInitContext() throws Exception {
        FakeServer httpSever = new FakeServer(9033);
        httpSever.loadSources();
        httpSever.loadContexts();
        List<Context> contexts = httpSever.getContexts();
        Context actual = contexts.get(0);
        assertNotNull(actual.getMapper().get("/").getServletClass());
        assertNull(actual.getMapper().get("/hello").getServletClass());

    }

    @Test
    public void httpServerCanHandleServletWhichLoadOnBootStrap() throws Exception {
        FakeServer httpSever = new FakeServer(9032);
        httpSever.start();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://localhost:9032/");
        CloseableHttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        String responseStr = EntityUtils.toString(entity);
        assertEquals("/",responseStr);

    }



}
