package info.xiantang.jerrymouse.core.pipeline;

import info.xiantang.jerrymouse.core.handler.ServletContext;
import info.xiantang.jerrymouse.http.core.HttpRequest;
import info.xiantang.jerrymouse.http.core.HttpResponse;

/**
 * @Author: xiantang
 * @Date: 2020/4/26 22:56
 */
public abstract class InBoundPipeline implements Pipeline{
    private Pipeline next;
    private ServletContext context;

    InBoundPipeline(ServletContext context) {
        this.context = context;
    }

    public ServletContext getContext() {
        return context;
    }

    public void setNext(Pipeline nextPipeline) {
        next = nextPipeline;
    }

    Pipeline getNext() {
        return next;
    }

    public abstract void doHandle(HttpRequest request, HttpResponse response);
}
