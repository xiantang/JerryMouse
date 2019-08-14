package com.github.apachefoundation.jerrymouse.container.valve.wapper;

import com.github.apachefoundation.jerrymouse.container.Contained;
import com.github.apachefoundation.jerrymouse.container.Container;
import com.github.apachefoundation.jerrymouse.container.valve.Valve;
import com.github.apachefoundation.jerrymouse.container.valve.ValveContext;
import com.github.apachefoundation.jerrymouse.container.wrapper.SimpleWrapper;
import com.github.apachefoundation.jerrymouse.http.HttpRequest;
import com.github.apachefoundation.jerrymouse.http.HttpResponse;
import com.github.apachefoundation.jerrymouse.servlet.HttpServlet;

import java.io.IOException;

/**
 * @Author: xiantang
 * @Date: 2019/5/24 21:30
 */
public class SimpleWrapperValve implements Valve, Contained {


    private Container container;

    public SimpleWrapperValve() {
    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public void invoke(HttpRequest request,
                       HttpResponse response,
                       ValveContext valveContext) throws IOException
    {
        SimpleWrapper simpleWrapper = (SimpleWrapper) getContainer();
        HttpServlet servlet = null;
        servlet = simpleWrapper.allocate();
        servlet.service(request, response);
    }
    @Override
    public void setContainer(Container container) {
        this.container = container;
    }
    @Override
    public Container getContainer() {
        return container;
    }

}
