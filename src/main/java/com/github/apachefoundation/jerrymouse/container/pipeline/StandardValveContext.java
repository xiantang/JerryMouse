package com.github.apachefoundation.jerrymouse.container.pipeline;

import com.github.apachefoundation.jerrymouse.container.valve.Valve;
import com.github.apachefoundation.jerrymouse.container.valve.ValveContext;
import com.github.apachefoundation.jerrymouse.http.HttpRequest;
import com.github.apachefoundation.jerrymouse.http.HttpResponse;


/**
 * @Author: xiantang
 * @Date: 2019/5/29 17:00
 */
public final class StandardValveContext implements ValveContext {
    protected int stage = 0;
    protected Valve basic = null;
    protected Valve[] valves = null;



    @Override
    public void setStage(int stage) {
        this.stage = stage;
    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public void invokeNext(HttpRequest request, HttpResponse response) throws Exception {
        int subscript = stage;
        stage += 1;
        if (subscript < valves.length) {
            valves[subscript].invoke(request, response, this);
        } else if ((subscript == valves.length)) {
            basic.invoke(request, response, this);
        } else {
            throw new Exception("standardPipeline.noValve");
        }
    }

    @Override
    public void set(Valve basic, Valve[] valves) {
        stage = 0;
        this.basic = basic;
        this.valves = valves;
    }
}
