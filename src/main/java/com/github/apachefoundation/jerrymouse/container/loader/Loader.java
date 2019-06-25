package com.github.apachefoundation.jerrymouse.container.loader;

import com.github.apachefoundation.jerrymouse.container.Container;
import org.xml.sax.helpers.DefaultHandler;

import javax.servlet.http.HttpServlet;
import java.lang.reflect.InvocationTargetException;

/**
 * @Author: xiantang
 * @Date: 2019/5/24 20:55
 */
public interface Loader {

    public HttpServlet load(String className) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException;

    public ClassLoader getClassLoader();

    public Container getContainer();

    public void setContainer();

    public void addRepository(String repository);

    public boolean modified();

    public void setReloadable(boolean reloadable);

    public boolean getReloadable();

    public void addPropertyChangeListener();

    public String[] findRepositories();
}
