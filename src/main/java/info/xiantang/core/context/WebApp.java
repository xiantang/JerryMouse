package info.xiantang.core.context;



import info.xiantang.core.network.endpoint.nio.NioEndpoint;
import org.apache.log4j.Logger;
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

public class WebApp {

    private static WebContext webContext;
    public static final String servletUrl = "file:target/test-classes/"; //放着servlet编译后的文件的文件夹地址
    private static Logger logger = Logger.getLogger(WebApp.class);
    /*
    初始化webContext存入servlet以及他的映射
     */

    static {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parse = factory.newSAXParser();
            WebHandler phandler = new WebHandler();
            // 当前线程的类加载器
            parse.parse(Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("web.xml"), phandler);
            webContext = new WebContext( phandler.getEntities(),phandler.getMappings());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过url返回特定的servlet

     * 这里更新了 改用 URLClassLoader
     * @param url
     * @return
     */
    public static HttpServlet getServletFromUrl(String url) {

        try {
            logger.info("訪問Url為:"+url);
            String className = webContext.getClz("/" + url);
            URL classUrl = new URL(servletUrl);
            ClassLoader classLoader = new URLClassLoader(new URL[]{classUrl});
            Class clz = classLoader.loadClass(className);
            HttpServlet servlet = (HttpServlet) clz.getConstructor().newInstance();
            return servlet;
        } catch (NullPointerException e) {
            logger.warn("頁面未找到");

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();


        } catch (MalformedURLException e) {
            e.printStackTrace();

        }
        return null;
    }
}