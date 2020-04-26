package info.xiantang.jerrymouse.core.pipeline;

import info.xiantang.jerrymouse.core.handler.ServletContext;
import info.xiantang.jerrymouse.http.core.HttpRequest;
import info.xiantang.jerrymouse.http.core.HttpResponse;

/**
 * @Author: xiantang
 * @Date: 2020/4/26 22:57
 */
public class TestInBoundPipeline extends InBoundPipeline {
    private ServletContext context;

    public TestInBoundPipeline(ServletContext context) {
        super(context);

    }

    @Override
    public void doHandle(HttpRequest request, HttpResponse response) {
        request.setMethod("POST");
    }
}
