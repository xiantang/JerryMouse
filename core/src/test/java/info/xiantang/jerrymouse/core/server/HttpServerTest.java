package info.xiantang.jerrymouse.core.server;

import info.xiantang.jerrymouse.core.conf.Configuration;
import info.xiantang.jerrymouse.core.utils.NetUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class HttpServerTest {

    @Test
    public void httpServerCanLoadJarPackage() throws Exception {
        HttpServer httpSever = new HttpServer();
        List<ServerSource> sources = httpSever.getServerSources();
        Map<String, ServletWrapper> router = new HashMap<>();
        ServletWrapper wrapper1 = new ServletWrapper(
                "index",
                "/",
                "info.xiantang.jerrymouse.staticresource.IndexServlet",
                0,
                null,
                null);
        ServletWrapper wrapper2 = new ServletWrapper(
                "hello",
                "/hello",
                "info.xiantang.jerrymouse.staticresource.HelloServlet",
                null,
                null,
                null);
        router.put("/", wrapper1);
        router.put("/hello", wrapper2);
        Configuration config = new Configuration(9000, 3, router);
        assertEquals(new ServerSource("sample.jar",config), sources.get(1));
    }


    @Test
    public void httpServerCanInitContext() throws Exception {
        HttpServer httpSever = new HttpServer("/tmp/static");
        httpSever.start();
        List<Context> contexts = httpSever.getContexts();
        Context actual = contexts.get(0);
        assertNotNull(actual.getMapper().get("/").getServletClass());

    }

    @Test
    public void httpServerCanHandleServletWhichLoadOnBootStrap() throws Exception {
        int availablePort = NetUtils.getAvailablePort();
        HttpServer httpSever = new HttpServer("/tmp/static");
        List<Context> contexts = httpSever.getContexts();
        Context context = contexts.get(0);
        context.setPort(availablePort);
        httpSever.start();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://localhost:" + availablePort + "/");
        CloseableHttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        String responseStr = EntityUtils.toString(entity);
        assertEquals("/",responseStr);
    }


    @Test
    public void httpServerCanHandleServletWhichNotLoadOnBootStrap() throws Exception {
        int availablePort = NetUtils.getAvailablePort();
        HttpServer httpSever = new HttpServer("/tmp/static");
        List<Context> contexts = httpSever.getContexts();
        Context context = contexts.get(0);
        context.setPort(availablePort);
        httpSever.start();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://localhost:" + availablePort + "/hello");
        CloseableHttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        String responseStr = EntityUtils.toString(entity);
        assertTrue(responseStr.contains("hello"));
    }


    @Test
    public void httpServerCanHandleServletWithIsNotFind() throws Exception {
        int availablePort = NetUtils.getAvailablePort();
        HttpServer httpSever = new HttpServer("/tmp/static");
        List<Context> contexts = httpSever.getContexts();
        Context context = contexts.get(0);
        context.setPort(availablePort);
        httpSever.start();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://localhost:" + availablePort + "/test");
        CloseableHttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        String responseStr = EntityUtils.toString(entity);
        assertEquals("404-NOT-FOUND",responseStr);

    }


}
