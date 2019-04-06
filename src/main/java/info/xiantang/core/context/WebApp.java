package info.xiantang.core.context;

import info.xiantang.core.servlet.Servlet;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.lang.reflect.InvocationTargetException;

public class WebApp {
    private static WebContext webContext;
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
                    .getResourceAsStream("info/xiantang/core/web.xml"), phandler);
            webContext = new WebContext( phandler.getEntities(),phandler.getMappings());
        } catch (Exception e) {
            System.out.println("解析配置文件错误");
        }
    }

    /**
     * 通过url返回特定的servlet
     * @param url
     * @return
     */
    public static Servlet getServletFromUrl(String url) {

        try {
            System.out.println(url);

            String className = webContext.getClz("/" + url);
            Class clz = Class.forName(className);
            Servlet servlet = (Servlet) clz.getConstructor().newInstance();
            return servlet;
        } catch (NullPointerException e) {
            System.out.println("頁面未找到");
//            e.printStackTrace();
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
        }
        return null;
    }
}
