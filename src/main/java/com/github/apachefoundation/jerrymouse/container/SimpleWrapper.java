package com.github.apachefoundation.jerrymouse.container;

import com.github.apachefoundation.jerrymouse.container.loader.Loader;
import com.github.apachefoundation.jerrymouse.container.loader.SimpleLoader;
import com.github.apachefoundation.jerrymouse.container.pipeline.Pipeline;
import com.github.apachefoundation.jerrymouse.container.pipeline.SimplePipeline;
import com.github.apachefoundation.jerrymouse.container.valve.SimpleWrapperValve;
import com.github.apachefoundation.jerrymouse.container.valve.Valve;
import com.github.apachefoundation.jerrymouse.http.HttpRequest;
import com.github.apachefoundation.jerrymouse.http.HttpResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

/**
 * @Author: xiantang
 * @Date: 2019/5/24 21:14
 */
public class SimpleWrapper implements Container, Wrapper, Pipeline {


    private Loader loader;
    private Container container;
    private SimplePipeline pipeline = new SimplePipeline(this);
    private HttpRequest request;
    private HttpResponse response;
    public SimpleWrapper() {
        // 先设置基础阀
        Valve swv = new SimpleWrapperValve();
        ((SimpleWrapperValve) swv).setContainer(this);
        pipeline.setBasic(swv);

    }

    @Override
    public void setLoader(Loader loader) {
        this.loader = loader;
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
    public Valve getBasic() {
        return pipeline.getBasic();
    }

    @Override
    public void setBasic(Valve valve) {
        pipeline.setBasic(valve);
    }

    @Override
    public void addValve(Valve valve) {
        pipeline.addValve(valve);
    }

    @Override
    public void removeValve(Valve valve) {
        pipeline.removeValve(valve);
    }


    @Override
    public HttpServlet allocate() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, MalformedURLException, ClassNotFoundException {
        SimpleLoader simpleLoader = (SimpleLoader) loader;
        return simpleLoader.load(request.getRequestURI());
    }


    @Override
    public void invoke(HttpRequest request, HttpResponse response) throws ServletException, IOException {
        this.request = request;
        this.response = response;
        pipeline.invoke(request,response);
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
