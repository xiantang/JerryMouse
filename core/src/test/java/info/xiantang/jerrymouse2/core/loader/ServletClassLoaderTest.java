package info.xiantang.jerrymouse2.core.loader;

import org.junit.Test;

import static org.junit.Assert.assertNotEquals;

public class ServletClassLoaderTest {
    @Test
    public void servletClassLoaderCanFindServlet() throws ClassNotFoundException {
        ServletClassLoader loader = new ServletClassLoader();
        String name = "info.xiantang.jerrymouse2.core.loader.HelloServlet";
        Class servletClass = loader.loadClass(name);
        assertNotEquals(null, servletClass);
    }
}
