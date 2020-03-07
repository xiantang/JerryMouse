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
        httpSever.init();
        List<ServerSource> sources = httpSever.getServerSources();
        Map<String, ServletWrapper> router = new HashMap<>();
        ServletWrapper wrapper1 = new ServletWrapper("index", "/", "xxx.xxxx.xxxxx.IndexServlet", 0, null,null);
        ServletWrapper wrapper2 = new ServletWrapper("reg", "/reg", "xxx.xxxx.xxxxx.RegServlet", null, null,null);
        router.put("/", wrapper1);
        router.put("/reg", wrapper2);
        List<ServerSource> expect = new ArrayList<>();
        Configuration config = new Configuration(9000, 3, router);
        expect.add(new ServerSource("test.jar",config));
        assertEquals(expect, sources);

    }




}
