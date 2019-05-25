package com.github.apachefoundation.jerrymouse.container;

import com.github.apachefoundation.jerrymouse.container.loader.Loader;
import com.github.apachefoundation.jerrymouse.container.pipeline.SimplePipeline;
import com.github.apachefoundation.jerrymouse.http.HttpRequest;
import com.github.apachefoundation.jerrymouse.http.HttpResponse;

import javax.servlet.http.HttpServlet;

/**
 * @Author: xiantang
 * @Date: 2019/5/24 21:14
 */
public class SimpleWrapper implements Container, Wrapper {
    private Loader loader;
    private Container container;
    private SimplePipeline pipeline = new SimplePipeline(this);


    public SimpleWrapper() {
    }

    @Override
    public Loader getLoader() {
        if (loader != null) {
            return loader;
        } else if (container != null) {
            return container.getLoader();
        }
        return null;
    }

    @Override
    public void load() {

    }


    @Override
    public HttpServlet allocate() {
        return null;
    }



    @Override
    public void invoke(HttpRequest request, HttpResponse response) {

    }

    @Override
    public void addChild(Container container) {

    }

    @Override
    public void removeChild(Container container) {

    }

    @Override
    public Container findChild(String name) {
        return null;
    }

    @Override
    public Container[] findChildren() {
        return new Container[0];
    }



}
