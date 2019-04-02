package info.xiantang.basic.servlet;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.List;

public class WebApp {
    private static WebContext webContext;
    static {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parse = factory.newSAXParser();
            WebHandler phandler = new WebHandler();
            // 当前线程的类加载器
            parse.parse(Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("info/xiantang/basic/servlet/web.xml"), phandler);
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
            String className = webContext.getClz("/" + url);
            Class clz = Class.forName(className);
            Servlet servlet = (Servlet) clz.getConstructor().newInstance();
            return servlet;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
