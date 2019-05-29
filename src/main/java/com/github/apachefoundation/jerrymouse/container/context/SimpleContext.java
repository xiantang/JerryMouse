package com.github.apachefoundation.jerrymouse.container.context;

import com.github.apachefoundation.jerrymouse.container.Container;
import com.github.apachefoundation.jerrymouse.container.loader.Loader;
import com.github.apachefoundation.jerrymouse.container.mapper.Mapper;
import com.github.apachefoundation.jerrymouse.container.pipeline.Pipeline;
import com.github.apachefoundation.jerrymouse.container.valve.Valve;
import com.github.apachefoundation.jerrymouse.http.HttpRequest;
import com.github.apachefoundation.jerrymouse.http.HttpResponse;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @Author: xiantang
 * @Date: 2019/5/29 16:24
 */
public class SimpleContext implements Context, Pipeline {

    @Override
    public Valve getBasic() {
        return null;
    }

    @Override
    public void setBasic(Valve valve) {

    }

    @Override
    public void addValve(Valve valve) {

    }

    @Override
    public void invoke(HttpRequest request, HttpResponse response) throws ServletException, IOException {

    }

    @Override
    public void removeValve(Valve valve) {

    }


    @Override
    public void addServletMapping(String pattern, String name) {

    }

    @Override
    public Mapper findMapper() {
        return null;
    }

    @Override
    public String findServletMapping(String uri) {
        return null;
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
    public void setLoader(Loader loader) {

    }

    @Override
    public Loader getLoader() {
        return null;
    }

    @Override
    public Container[] findChildren() {
        return new Container[0];
    }
}
