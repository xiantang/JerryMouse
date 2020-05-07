package info.xiantang.jerrymouse.core.pipeline;

import info.xiantang.jerrymouse.core.handler.ServletContext;
import info.xiantang.jerrymouse.http.core.HttpRequest;
import info.xiantang.jerrymouse.http.core.HttpResponse;

public class ContentOutBoundPipeline extends BoundPipeline {
    public ContentOutBoundPipeline(ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    public void doHandle(HttpRequest request, HttpResponse response) {
        int length = response.getBodyBytes().length;
        response.addHeader("Content-Length",length+"");
    }
}
