package com.github.apachefoundation.jerrymouse.container.pipeline;

import com.github.apachefoundation.jerrymouse.container.Wrapper;
import com.github.apachefoundation.jerrymouse.container.valve.Valve;
import com.github.apachefoundation.jerrymouse.http.HttpRequest;
import com.github.apachefoundation.jerrymouse.http.HttpResponse;

/**
 * @Author: xiantang
 * @Date: 2019/5/24 21:23
 */
public class SimplePipeline implements Pipeline {

    private Wrapper wrapper;

    public SimplePipeline(Wrapper wrapper) {
        this.wrapper = wrapper;
    }

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
    public void invoke(HttpRequest request, HttpResponse response) {

    }

    @Override
    public void removeValve(Valve valve) {

    }
}
