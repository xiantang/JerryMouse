package com.github.apachefoundation.jerrymouse.container.pipeline;

import com.github.apachefoundation.jerrymouse.container.Wrapper;
import com.github.apachefoundation.jerrymouse.container.valve.Valve;
import com.github.apachefoundation.jerrymouse.container.valve.ValveContext;
import com.github.apachefoundation.jerrymouse.http.HttpRequest;
import com.github.apachefoundation.jerrymouse.http.HttpResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: xiantang
 * @Date: 2019/5/24 21:23
 */
public class SimplePipeline implements Pipeline {

    private Wrapper wrapper;
    private Valve basic;
    private List<Valve> valves = new ArrayList<>();
    private StandardValveContext standardValveContext;
//    private


    public SimplePipeline() {
        this.standardValveContext = new StandardValveContext();

    }

    public SimplePipeline(Wrapper wrapper) {
        this();
        this.wrapper = wrapper;
    }

    @Override
    public Valve getBasic() {
        return basic;
    }

    @Override
    public void setBasic(Valve basic) {
        this.basic = basic;
        standardValveContext.set(getBasic(),(Valve[]) valves.toArray(new Valve[valves.size()]));
    }

    @Override
    public void addValve(Valve valve) {
        valves.add(valve);
        standardValveContext.set(getBasic(),(Valve[]) valves.toArray(new Valve[valves.size()]));
    }

    @Override
    public void invoke(HttpRequest request, HttpResponse response) throws ServletException, IOException {
        standardValveContext.invokeNext(request, response);
    }

    @Override
    public void removeValve(Valve valve) {
        valves.remove(valve);
    }


    public final class StandardValveContext implements ValveContext {
        protected int stage = 0;
        protected Valve basic = null;
        protected Valve[] valves = null;

        @Override
        public String getInfo() {
            return null;
        }

        @Override
        public void invokeNext(HttpRequest request, HttpResponse response) throws ServletException, IOException {
            int subscript = stage;
            stage += 1;
            if (subscript < valves.length) {
                valves[subscript].invoke(request, response, this);
            } else if ((subscript == valves.length)) {
                basic.invoke(request, response, this);
            } else {
                throw new ServletException("standardPipeline.noValve");
            }
        }

        public void set(Valve basic, Valve[] valves) {
            stage = 0;
            this.basic = basic;
            this.valves = valves;
        }
    }
}

