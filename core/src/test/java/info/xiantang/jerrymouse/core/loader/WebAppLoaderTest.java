package info.xiantang.jerrymouse.core.loader;

import info.xiantang.jerrymouse.core.lifecycle.LifeCycle;
import info.xiantang.jerrymouse.core.server.ServletWrapper;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class WebAppLoaderTest {

    private void initRouter(Map<String, ServletWrapper> router) {
        ServletWrapper wrapper1 =
                new ServletWrapper("hello",
                        "/hello",
                        "info.xiantang.jerrymouse.core.loader.HelloServlet",
                        0,
                        null,
                        null);
        ServletWrapper wrapper2 =
                new ServletWrapper("index",
                        "/",
                        "info.xiantang.jerrymouse.core.loader.IndexServlet",
                        null,
                        null,
                        null);
        router.put("/hello", wrapper1);
        router.put("/", wrapper2);
    }

    private void initExpect(Map<String, ServletWrapper> expect) {
        ServletWrapper wrapper3 =
                new ServletWrapper("hello",
                        "/hello",
                        "info.xiantang.jerrymouse.core.loader.HelloServlet",
                        0,
                        HelloServlet.class,
                        new HelloServlet());
        ServletWrapper wrapper4 =
                new ServletWrapper("index",
                        "/",
                        "info.xiantang.jerrymouse.core.loader.IndexServlet",
                        null,
                        null,
                        null);
        expect.put("/hello", wrapper3);
        expect.put("/", wrapper4);
    }

    @Test
    public void webAppLoaderCanLoadInstanceOfLoadOnStartUp() throws Exception {
        ClassLoader classLoader = new ServletClassLoader();
        Thread.currentThread().setContextClassLoader(classLoader);
        Map<String, ServletWrapper> router = new HashMap<>();
        initRouter(router);
        WebAppLoader loader = new WebAppLoader(router, classLoader);
        Map<String, ServletWrapper> actual = loader.loadOnStartUp();
        Map<String, ServletWrapper> expect = new HashMap<>();
        initExpect(expect);
        assertEquals(expect, actual);

    }



    @Test(expected = NullPointerException.class)
    public void testWebAppLoaderInitAndStop() throws Exception {
        Map<String, ServletWrapper> router = new HashMap<>();
        initRouter(router);
        ClassLoader classLoader = new JarClassLoader("sample.jar");
        LifeCycle loader = new WebAppLoader(router, classLoader);
        loader.init();
        Map<String, ServletWrapper> expect = new HashMap<>();
        initExpect(expect);
        assertEquals(expect, ((WebAppLoader)loader).getRouter());
        loader.destroy();
        ((WebAppLoader) loader).getRouter();
    }

}
