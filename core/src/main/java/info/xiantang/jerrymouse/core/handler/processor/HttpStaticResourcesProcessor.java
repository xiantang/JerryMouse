package info.xiantang.jerrymouse.core.handler.processor;

import info.xiantang.jerrymouse.core.handler.HandlerContext;
import info.xiantang.jerrymouse.core.utils.FileUtils;
import info.xiantang.jerrymouse.http.core.HttpRequest;
import info.xiantang.jerrymouse.http.core.HttpResponse;

import java.io.File;
import java.io.OutputStream;

public class HttpStaticResourcesProcessor implements Processor {
    private HandlerContext context;

    HttpStaticResourcesProcessor(HandlerContext context) {
        this.context = context;
    }

    @Override
    public void process(HttpRequest request, HttpResponse response) throws Exception {
        String path = request.getPath();
        String resourcePath = context.getResourcePath();
        File file = new File(resourcePath + path);
        byte[] bytes = FileUtils.readBytes(file);
        response.addHeader("Content-Type","image/ico");
        OutputStream outputStream = response.getResponseOutputStream();
        outputStream.write(bytes);
    }
}
