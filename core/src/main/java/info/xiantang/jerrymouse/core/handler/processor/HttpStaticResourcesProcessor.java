package info.xiantang.jerrymouse.core.handler.processor;

import info.xiantang.jerrymouse.core.utils.FileUtils;
import info.xiantang.jerrymouse.http.core.HttpRequest;
import info.xiantang.jerrymouse.http.core.HttpResponse;

import java.io.InputStream;
import java.io.OutputStream;

public class HttpStaticResourcesProcessor implements Processor {

    HttpStaticResourcesProcessor() {
    }

    @Override
    public void process(HttpRequest request, HttpResponse response) throws Exception {
        String path = request.getPath().replaceFirst("/", "");
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(path);
        byte[] bytes = FileUtils.readBytes(resourceAsStream);
        response.addHeader("Content-Type","image/ico");
        OutputStream outputStream = response.getResponseOutputStream();
        outputStream.write(bytes);
    }
}
