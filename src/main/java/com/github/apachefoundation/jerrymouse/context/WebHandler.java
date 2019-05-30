package com.github.apachefoundation.jerrymouse.context;


import com.github.apachefoundation.jerrymouse.container.wrapper.SimpleWrapper;
import com.github.apachefoundation.jerrymouse.container.wrapper.Wrapper;
import com.github.apachefoundation.jerrymouse.entity.Mapping;
import com.github.apachefoundation.jerrymouse.entity.Entity;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


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
    public void startDocument() throws SAXException {
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
    public void characters(char[] ch, int start, int length) throws SAXException {
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
    public void endElement(String uri, String localName, String qName) throws SAXException {
        String servlet = "servlet";
        String servletMapping = "servlet-mapping";
        if (qName != null) {
            if (qName.equals(servlet)) {
                wrappers.add((Wrapper) wrapper);
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
            WebHandler phandler = new WebHandler();
            // 当前线程的类加载器
            parse.parse(Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("web.xml"), phandler);
            for (Wrapper w : phandler.getWrappers()
            ) {
                System.out.println(w.getName());
                System.out.println(w.getServletClass());
            }

            for (Mapping m :
                    phandler.getMappings()) {
                System.out.println(m.getName());
                System.out.println(m.getPatterns());

            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
