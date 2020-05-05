package info.xiantang.jerrymouse.core.handler.processor;

import info.xiantang.jerrymouse.core.handler.ServletContext;
import info.xiantang.jerrymouse.core.pipeline.InBoundPipeline;
import info.xiantang.jerrymouse.core.utils.StaticResourcesUtils;
import info.xiantang.jerrymouse.http.core.HttpRequest;
import info.xiantang.jerrymouse.http.core.HttpResponse;

public class HttpDispatchProcessor implements Processor {
    private ServletContext context;


    public HttpDispatchProcessor(ServletContext context) {
        this.context = context;
    }

    @Override
    public void process(HttpRequest request, HttpResponse response) throws Exception {
        Processor processor;
        boolean isStatic = StaticResourcesUtils.verifyPath(request.getPath());
        if (isStatic) {
            processor = new HttpStaticResourcesProcessor(context);
        } else {
            processor = new HttpServletProcessor(context);
        }
        // inbound pipeline
        InBoundPipeline inboundPipeline = context.getInboundPipeline();
        inboundPipeline.doHandle(request, response);
        processor.process(request, response);
        //TODO outbound pipeline
    }
}
