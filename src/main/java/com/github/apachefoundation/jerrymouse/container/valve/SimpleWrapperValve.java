package com.github.apachefoundation.jerrymouse.container.valve;

import com.github.apachefoundation.jerrymouse.container.Contained;
import com.github.apachefoundation.jerrymouse.container.Container;
import com.github.apachefoundation.jerrymouse.container.wrapper.SimpleWrapper;
import com.github.apachefoundation.jerrymouse.http.HttpRequest;
import com.github.apachefoundation.jerrymouse.http.HttpResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

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
                       ValveContext valveContext
    ) throws ServletException, IOException {
        SimpleWrapper simpleWrapper = (SimpleWrapper) getContainer();
        HttpServlet servlet = null;
        try {
            servlet = simpleWrapper.allocate();
            servlet.service(request, response);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

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
