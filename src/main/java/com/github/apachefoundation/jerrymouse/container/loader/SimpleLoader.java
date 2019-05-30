package com.github.apachefoundation.jerrymouse.container.loader;

import com.github.apachefoundation.jerrymouse.container.Container;
import com.github.apachefoundation.jerrymouse.context.WebContext;
import com.github.apachefoundation.jerrymouse.context.WebHandler;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServlet;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @Author: xiantang
 * @Date: 2019/5/24 20:55
 */
public class SimpleLoader implements Loader {
    public static final String WEB_ROOT = "file:target/test-classes/";
    private ClassLoader classLoader = null;
    private Container container = null;


    public SimpleLoader() {
        try {
            URL classUrl = new URL(WEB_ROOT);
            classLoader = new URLClassLoader(new URL[]{classUrl});
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public HttpServlet load(String className) throws MalformedURLException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        URL classUrl = new URL(WEB_ROOT);
        Class clz = classLoader.loadClass(className);
        HttpServlet servlet = (HttpServlet) clz.getConstructor().newInstance();
        return servlet;
    }
}
