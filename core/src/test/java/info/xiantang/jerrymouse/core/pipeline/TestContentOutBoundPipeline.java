package info.xiantang.jerrymouse.core.pipeline;

import info.xiantang.jerrymouse.core.handler.ServletContext;
import info.xiantang.jerrymouse.http.core.HttpRequest;
import info.xiantang.jerrymouse.http.core.HttpResponse;

public class TestContentOutBoundPipeline extends BoundPipeline {
    public TestContentOutBoundPipeline(ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    public void doHandle(HttpRequest request, HttpResponse response) {
        response.addHeader("Content-Length",3+"");
    }
}
