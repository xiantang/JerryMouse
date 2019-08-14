package com.github.apachefoundation.jerrymouse.container.pipeline;

import com.github.apachefoundation.jerrymouse.container.Container;
import com.github.apachefoundation.jerrymouse.container.valve.Valve;
import com.github.apachefoundation.jerrymouse.container.valve.ValveContext;
import com.github.apachefoundation.jerrymouse.http.HttpRequest;
import com.github.apachefoundation.jerrymouse.http.HttpResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: xiantang
 * @Date: 2019/5/24 21:23
 */
public class SimplePipeline implements Pipeline {

    private Container container;
    private Valve basic;
    private List<Valve> valves = new ArrayList<>();
    private ValveContext valveContext;
//    private


    public SimplePipeline(ValveContext valveContext) {
        this.valveContext = valveContext;

    }


    public SimplePipeline(Container container,ValveContext valveContext) {
        this(valveContext);
        this.container = container;
    }

    @Override
    public Valve getBasic() {
        return basic;
    }

    @Override
    public void setBasic(Valve basic) {
        this.basic = basic;
        valveContext.set(getBasic(),(Valve[]) valves.toArray(new Valve[valves.size()]));
    }

    @Override
    public void addValve(Valve valve) {
        valves.add(valve);
        valveContext.set(getBasic(),(Valve[]) valves.toArray(new Valve[valves.size()]));
    }

    @Override
    public void invoke(HttpRequest request, HttpResponse response) throws Exception {
        // 每次调用把stage设置为0
        valveContext.setStage(0);
        valveContext.invokeNext(request, response);
    }

    @Override
    public void removeValve(Valve valve) {
        valves.remove(valve);
    }



}

