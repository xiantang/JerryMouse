package com.github.apachefoundation.jerrymouse.container.wrapper;

import com.github.apachefoundation.jerrymouse.servlet.HttpServlet;
import com.github.apachefoundation.jerrymouse.container.Container;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

/**
 * @Author: xiantang
 * @Date: 2019/5/24 20:17
 */
public interface Wrapper extends Container {
    /**
     * 分配一个已经初始化的实例
     * @return
     */
    HttpServlet allocate() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, MalformedURLException, ClassNotFoundException;

    void setServletClass(String servletClass);

    void load() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, MalformedURLException, ClassNotFoundException;

    Container getContainer();

    void setContainer(Container container);

    String getName();

    void setName(String name);

    String getServletClass();

}
