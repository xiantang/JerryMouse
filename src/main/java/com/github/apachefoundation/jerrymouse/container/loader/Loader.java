package com.github.apachefoundation.jerrymouse.container.loader;

import com.github.apachefoundation.jerrymouse.servlet.HttpServlet;
import com.github.apachefoundation.jerrymouse.container.Container;

import java.lang.reflect.InvocationTargetException;

/**
 * @Author: xiantang
 * @Date: 2019/5/24 20:55
 */
public interface Loader {

    HttpServlet load(String className) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException;

    Container getContainer();

    void setContainer(Container context);

}
