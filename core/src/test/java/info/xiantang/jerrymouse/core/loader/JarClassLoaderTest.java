package info.xiantang.jerrymouse.core.loader;

import info.xiantang.jerrymouse.http.servlet.Servlet;
import org.junit.Test;

import java.lang.reflect.Constructor;

import static org.junit.Assert.*;

public class JarClassLoaderTest {

    @Test
    public void jarClassLoaderCanLoadSampleJarPackage() throws Exception {
        JarClassLoader loader = new JarClassLoader("sample.jar");
        Class clz = loader.loadClass("info.xiantang.jerrymouse.staticresource.HelloServlet");
        Constructor declaredConstructor = clz.getDeclaredConstructor();
        declaredConstructor.setAccessible(true);
        Object obj = declaredConstructor.newInstance();
        assertTrue(obj instanceof Servlet);
    }
}
