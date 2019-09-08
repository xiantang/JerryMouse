package com.github.apachefoundation.jerrymouse.context;


import com.github.apachefoundation.jerrymouse.container.wrapper.SimpleWrapper;
import com.github.apachefoundation.jerrymouse.container.wrapper.Wrapper;
import com.github.apachefoundation.jerrymouse.entity.Mapping;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * @Author: xiantang
 * @Date: 2019/4/17 14:45
 * 负责解析web.xml
 */
public class WebHandler extends DefaultHandler {
    private List<Wrapper> wrappers;
    private List<Mapping> mappings;
    private Mapping mapping;
    private Wrapper wrapper;
    /**
     * 存储操作标签
     */
    private String tag;
    private boolean isMapping = false;

    @Override
    public void startDocument() {
        wrappers = new ArrayList<>();
        mappings = new ArrayList<>();
    }


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        String servlet = "servlet";
        String servletMapping = "servlet-mapping";
        if (qName != null) {
            tag = qName;
            if (tag.equals(servlet)) {
                wrapper = new SimpleWrapper();
                isMapping = false;
            } else if (tag.equals(servletMapping)) {
                mapping = new Mapping();
                isMapping = true;
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length)  {
        String servletName = "servlet-name";
        String urlPattern = "url-pattern";
        String servletClass = "servlet-class";
        String contents = new String(ch, start, length);
        // 处理了空
        if (tag != null) {
            // 操作servlet-mapping
            if (isMapping) {
                if (servletName.equals(tag)) {
                    mapping.setName(contents);
                } else if (urlPattern.equals(tag)) {
                    mapping.addPattern(contents);
                }
            } else {
                if (servletName.equals(tag)) {
                    wrapper.setName(contents);
                } else if (servletClass.equals(tag)) {
                    wrapper.setServletClass(contents);
                }
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        String servlet = "servlet";
        String servletMapping = "servlet-mapping";
        if (qName != null) {
            if (qName.equals(servlet)) {
                wrappers.add(wrapper);
            } else if (qName.equals(servletMapping)) {
                mappings.add(mapping);
            }
        }
        tag = null;
    }

    public List<Wrapper> getWrappers() {
        return wrappers;
    }

    public List<Mapping> getMappings() {
        return mappings;
    }

    public static void main(String[] args) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parse = factory.newSAXParser();
            WebHandler pHandler = new WebHandler();
            // 当前线程的类加载器
            parse.parse(Objects.requireNonNull(Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("web.xml")), pHandler);
            for (Wrapper w : pHandler.getWrappers()
            ) {
                System.out.println(w.getName());
                System.out.println(w.getServletClass());
            }

            for (Mapping m :
                    pHandler.getMappings()) {
                System.out.println(m.getName());
                System.out.println(m.getPatterns());

            }

        } catch (Exception e) {
            // TODO 替换为log的error 方便在日志中打印
            e.printStackTrace();
        }
    }
}
