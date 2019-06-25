package com.github.apachefoundation.jerrymouse.container.loader;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

/**
 * @Author: xiantang
 * @Date: 2019/6/24 22:09
 */
public class WebappClassLoader extends URLClassLoader implements Reloader {
    public WebappClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public WebappClassLoader(URL[] urls) {
        super(urls);
    }

    public WebappClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
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


}
