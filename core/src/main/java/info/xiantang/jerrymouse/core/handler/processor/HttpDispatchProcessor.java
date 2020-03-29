package info.xiantang.jerrymouse.core.handler.processor;

import info.xiantang.jerrymouse.core.handler.HandlerContext;
import info.xiantang.jerrymouse.http.core.HttpRequest;
import info.xiantang.jerrymouse.http.core.HttpResponse;

import java.util.Map;

public class HttpDispatchProcessor implements Processor {
    private HandlerContext context;


    public HttpDispatchProcessor(HandlerContext context) {
        this.context = context;
    }

    @Override
    public void process(HttpRequest request, HttpResponse response) throws Exception {
        Processor processor;
        Map<String, String> headers = request.getHeaders();
        String dest = headers.get("Sec-Fetch-Dest");
        if ("image".equals(dest)) {
            processor = new HttpStaticResourcesProcessor(context);
        } else {
            processor = new HttpServletProcessor(context);
        }
        processor.process(request, response);
    }
}
