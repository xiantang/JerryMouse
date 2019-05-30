package com.github.apachefoundation.jerrymouse.context;


import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

/**
 * @Author: xiantang
 * @Date: 2019/4/17 14:45
 */
public class WebApp {

    private static WebContext webContext;
    /**
     *放着servlet编译后的文件的文件夹地址
     */
    public static final String URL = "file:target/test-classes/";
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
//            webContext = new WebContext( phandler.getWrappers(),phandler.getMappings());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }


}