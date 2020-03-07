package info.xiantang.jerrymouse.core.server;

import info.xiantang.jerrymouse.core.conf.Configuration;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

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
    public void httpServerCanInitContext() throws IOException {
        HttpServer httpSever = new HttpServer();
        httpSever.loadSources();
        httpSever.loadContexts();
        List<Context> contexts = httpSever.getContexts();
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
        Configuration configuration = new Configuration(123, 3, router);
        Context expect = new Context("2345",  configuration);
        Context actual = contexts.get(0);
        assertEquals(actual.getMapper(),expect.getMapper());
    }



}
