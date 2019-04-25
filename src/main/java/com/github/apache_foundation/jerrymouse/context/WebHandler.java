package com.github.apache_foundation.jerrymouse.context;


import com.github.apache_foundation.jerrymouse.entity.Mapping;
import com.github.apache_foundation.jerrymouse.entity.Entity;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;


/**
 * @Author: xiantang
 * @Date: 2019/4/17 14:45
 * 负责解析web.xml
 */
public class WebHandler extends DefaultHandler {
    private List<Entity> entities;
    private List<Mapping> mappings;
    private Mapping mapping;
    private Entity entity;
    /**
     * 存储操作标签
     */
    private String tag;
    private boolean isMapping = false;

    @Override
    public void startDocument() throws SAXException {
        entities = new ArrayList<>();
        mappings = new ArrayList<>();
    }


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        String servlet = "servlet";
        String servletMapping = "servlet-mapping";
        if (qName != null) {
            tag = qName;
            if (tag.equals(servlet)) {
                entity = new Entity();
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
                    entity.setName(contents);
                } else if (servletClass.equals(tag)) {
                    entity.setClz(contents);
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
                entities.add(entity);
            } else if (qName.equals(servletMapping)) {
                mappings.add(mapping);
            }
        }
        tag = null;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public List<Mapping> getMappings() {
        return mappings;
    }
}
