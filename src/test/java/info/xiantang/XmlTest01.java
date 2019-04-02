package info.xiantang;

import info.xiantang.basic.servlet.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class XmlTest01 {
    public static void main(String[] args) throws Exception{
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parse = factory.newSAXParser();
        WebHandler phandler = new WebHandler();
        // 当前线程的类加载器
        parse.parse(Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("info/xiantang/basic/servlet/web.xml"), phandler);
        List<Entity> entities = phandler.getEntities();
        List<Mapping> mappings = phandler.getMappings();
        WebContext webContext = new WebContext(entities, mappings);
        String className = webContext.getClz("/xx");
        Class clz =  Class.forName(className);
        Servlet servlet = (Servlet) clz.getConstructor().newInstance();
//        servlet.service();

    }

}

class Phandler extends DefaultHandler {
    @Override
    public void startDocument() throws SAXException {
        System.out.println("开始");
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        System.out.println(qName + " 解析结束");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        System.out.println(qName + " 解析开始");
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

        String contents = new String(ch, start, length).trim();
//        if ()
//        if (contents.length() > 0) {
//            System.out.println("内容为"+contents.trim());
//
//        }
//        else  System.out.println("内容为空");

    }

    @Override
    public void endDocument() throws SAXException {
        System.out.println("结束");
    }
}