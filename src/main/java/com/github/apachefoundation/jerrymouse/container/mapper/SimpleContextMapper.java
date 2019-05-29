package com.github.apachefoundation.jerrymouse.container.mapper;

import com.github.apachefoundation.jerrymouse.container.Container;
import com.github.apachefoundation.jerrymouse.container.context.SimpleContext;
import com.github.apachefoundation.jerrymouse.container.wrapper.Wrapper;
import com.github.apachefoundation.jerrymouse.http.HttpRequest;

/**
 * @Author: xiantang
 * @Date: 2019/5/29 19:24
 */
public class SimpleContextMapper implements Mapper {
    private SimpleContext context = null;

    @Override
    public Container getContainer() {
        return context;
    }

    @Override
    public void setContainer(Container container) {
        this.context = (SimpleContext) container;
    }

    @Override
    public void setProtocol(String protocol) {

    }

    @Override
    public String getProtocol() {
        return null;
    }

    @Override
    public Container map(HttpRequest request, boolean update) {
        String relativeUrl = request.getRequestURI();
        Wrapper wrapper = null;
        String name = context.findServletMapping(relativeUrl);
        if (name == null) {
            wrapper = (Wrapper) context.findChild(name);
        }
        return wrapper;
    }
}
