package info.xiantang.jerrymouse.core.pipeline;

import info.xiantang.jerrymouse.core.handler.ServletContext;
import info.xiantang.jerrymouse.http.core.HttpRequest;
import info.xiantang.jerrymouse.http.core.HttpResponse;

/**
 * @Author: xiantang
 * @Date: 2020/4/26 22:57
 */
public class RootInBoundPipeline extends InBoundPipeline {
    private ServletContext context;

    public RootInBoundPipeline(ServletContext context) {
        super(context);
    }

    @Override
    public void doHandle(HttpRequest request, HttpResponse response) {
        Pipeline nextPipeLine = getNext();
        if (nextPipeLine != null) {
            nextPipeLine.doHandle(request, response);
        }
    }
}
