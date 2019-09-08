package com.github.apachefoundation.jerrymouse.container.valve.context;

import com.github.apachefoundation.jerrymouse.container.Contained;
import com.github.apachefoundation.jerrymouse.container.Container;
import com.github.apachefoundation.jerrymouse.container.context.Context;
import com.github.apachefoundation.jerrymouse.container.valve.Valve;
import com.github.apachefoundation.jerrymouse.container.valve.ValveContext;
import com.github.apachefoundation.jerrymouse.container.wrapper.Wrapper;
import com.github.apachefoundation.jerrymouse.http.HttpRequest;
import com.github.apachefoundation.jerrymouse.http.HttpResponse;

import java.io.IOException;

/**
 * Context实例的基础阀
 * @Author: xiantang
 * @Date: 2019/5/29 17:03
 */
public class SimpleContextValve implements Valve, Contained {
    private Container container;

    @Override
    public Container getContainer() {
        return container;
    }

    @Override
    public void setContainer(Container container) {
        this.container = container;
    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public void invoke(HttpRequest request, HttpResponse response, ValveContext valveContext) throws Exception {
        Context context = (Context) getContainer();
        Wrapper wrapper;
        try {
            wrapper = (Wrapper) context.map(request, true);
        } catch (IllegalArgumentException e) {
            //TODO 包装成为bad Request
            e.printStackTrace();
            return;
        }
        if (wrapper == null) {
            request.setRequestURI("404");
            wrapper = (Wrapper) context.map(request, true);
        }
        wrapper.invoke(request, response);
    }
}
