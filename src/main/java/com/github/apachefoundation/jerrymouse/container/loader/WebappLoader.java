package com.github.apachefoundation.jerrymouse.container.loader;

import com.github.apachefoundation.jerrymouse.container.Container;

import javax.servlet.http.HttpServlet;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @Author: xiantang
 * @Date: 2019/6/24 21:56
 */
public class WebappLoader extends Thread implements Loader {



    private ClassLoader parentClassLoader;


    private ClassLoader classLoader = null;
    public static final String WEB_ROOT = "file:target/test-classes/";


    @Override
    public synchronized void start() {
        super.start();
    }


    public WebappLoader() {
        try {
            URL classUrl = new URL(WEB_ROOT);
            classLoader = new URLClassLoader(new URL[]{classUrl});
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private WebappClassLoader createClassLoader() throws Exception {
        Class clazz = Class.forName("");
        WebappClassLoader classLoader = null;
        if (parentClassLoader == null) {
            classLoader = (WebappClassLoader) clazz.newInstance();
        } else {
            Class[] argTypes = {ClassLoader.class};
            Object[] args = {parentClassLoader};
            Constructor constructor = clazz.getConstructor(argTypes);
            classLoader = (WebappClassLoader) constructor.newInstance(args);
        }
        return classLoader;
    }


    @Override
    public HttpServlet load(String className) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class clz = classLoader.loadClass(className);
        HttpServlet servlet = (HttpServlet) clz.getConstructor().newInstance();
        return servlet;
    }

    @Override
    public ClassLoader getClassLoader() {
        return null;
    }

    @Override
    public Container getContainer() {
        return null;
    }

    @Override
    public void setContainer() {

    }

    @Override
    public void addRepository(String repository) {

    }

    @Override
    public String[] findRepository() {
        return new String[0];
    }

    @Override
    public boolean modified() {
        return false;
    }

    @Override
    public void setReloadable(boolean reloadable) {

    }

    @Override
    public boolean getReloadable() {
        return false;
    }

    @Override
    public void addPropertyChangeListener() {

    }

    @Override
    public String[] findRepositories() {
        return new String[0];
    }
}
