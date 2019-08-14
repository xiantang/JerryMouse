package com.github.apachefoundation.jerrymouse.container.wrapper;

import com.github.apachefoundation.jerrymouse.http.HttpRequest;
import com.github.apachefoundation.jerrymouse.servlet.HttpServlet;
import com.github.apachefoundation.jerrymouse.container.Container;
import com.github.apachefoundation.jerrymouse.container.loader.Loader;
import com.github.apachefoundation.jerrymouse.container.loader.WebappLoader;
import com.github.apachefoundation.jerrymouse.container.pipeline.Pipeline;
import com.github.apachefoundation.jerrymouse.container.pipeline.SimplePipeline;
import com.github.apachefoundation.jerrymouse.container.pipeline.StandardValveContext;
import com.github.apachefoundation.jerrymouse.container.valve.Valve;
import com.github.apachefoundation.jerrymouse.http.HttpResponse;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;


/**
 * @Author: xiantang
 * @Date: 2019/5/24 21:14
 */
public class SimpleWrapper implements Wrapper, Pipeline {


    private Loader loader;
    private Container container;
    private SimplePipeline pipeline = new SimplePipeline(this, new StandardValveContext());

    /**
     * 设置映射
     * name servlet的name
     * servlet-class servlet目标的类文件
     */
    private String name;
    private String servletClass;
    private HttpServlet servlet = null;

    @Override
    public Container getContainer() {
        return container;
    }

    @Override
    public void setContainer(Container container) {
        this.container = container;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getServletClass() {
        return servletClass;
    }
    @Override
    public void setServletClass(String servletClass) {
        this.servletClass = servletClass;
    }

    public SimpleWrapper() {
        // 先设置基础阀
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
    public void load() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, ClassNotFoundException {

        WebappLoader simpleLoader = (WebappLoader) loader;
        servlet = simpleLoader.load(servletClass);
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
    public HttpServlet allocate(){
        if (servlet != null) {
            return servlet;
        }
        return null;
    }


    @Override
    public void invoke(HttpRequest request, HttpResponse response) throws Exception {
        pipeline.invoke(request, response);
    }

    @Override
    public void addChild(Container container) {
        throw new NotImplementedException();

    }

    @Override
    public void removeChild(Container container) {
        throw new NotImplementedException();

    }

    @Override
    public Container findChild(String name) {
        throw new NotImplementedException();

    }

    @Override
    public Container[] findChildren() {
        throw new NotImplementedException();
    }


}
