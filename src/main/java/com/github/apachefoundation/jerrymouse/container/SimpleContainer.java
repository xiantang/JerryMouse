package com.github.apachefoundation.jerrymouse.container;

import com.github.apachefoundation.jerrymouse.container.loader.Loader;
import com.github.apachefoundation.jerrymouse.context.WebContext;
import com.github.apachefoundation.jerrymouse.context.WebHandler;
import com.github.apachefoundation.jerrymouse.http.HttpRequest;
import com.github.apachefoundation.jerrymouse.http.HttpResponse;
import com.github.apachefoundation.jerrymouse.http.RequestFacade;
import com.github.apachefoundation.jerrymouse.http.ResponseFacade;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
 * @Date: 2019/5/24 20:26
 */
public class SimpleContainer implements Container {


    private static WebContext webContext;
    /**
     *放着servlet编译后的文件的文件夹地址
     */
    public static final String URL = "file:target/test-classes/";
    private static Logger logger = Logger.getLogger(SimpleContainer.class);
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


    @Override
    public void invoke(HttpRequest request, HttpResponse response) throws ServletException, IOException {
        Servlet servlet = null;
        servlet = (HttpServlet) getServletFromUrl(request.getRequestURI());
        HttpServletRequest requestFacade = new RequestFacade(request);
        HttpServletResponse responseFacade = new ResponseFacade(response);
        if (servlet != null) {
            servlet.service(requestFacade, responseFacade);
        } else {
            servlet = (HttpServlet) getServletFromUrl("404");
            servlet.service(requestFacade, responseFacade);
        }


    }

    @Override
    public Loader getLoader() {
        return null;
    }
    @Override
    public void setLoader(Loader loader) {

    }

    private HttpServlet getServletFromUrl(String url){

        try {
            String className = webContext.getClz("/" + url);
            URL classUrl = new URL(URL);
            // 创建一个类加载器
            ClassLoader classLoader = new URLClassLoader(new URL[]{classUrl});
            Class clz = classLoader.loadClass(className);
            HttpServlet servlet = (HttpServlet) clz.getConstructor().newInstance();
            return servlet;
        } catch (NullPointerException e) {
            logger.debug("页面未找到");

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
    @Override
    public void addChild(Container container) {

    }

    @Override
    public void removeChild(Container container) {

    }

    @Override
    public Container findChild(String name) {
        return null;
    }

    @Override
    public Container[] findChildren() {
        return new Container[0];
    }
}
