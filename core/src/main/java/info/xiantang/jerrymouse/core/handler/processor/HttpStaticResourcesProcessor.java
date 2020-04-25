package info.xiantang.jerrymouse.core.handler.processor;

import info.xiantang.jerrymouse.core.handler.HandlerContext;
import info.xiantang.jerrymouse.core.utils.FileUtils;
import info.xiantang.jerrymouse.http.core.HttpRequest;
import info.xiantang.jerrymouse.http.core.HttpResponse;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public class HttpStaticResourcesProcessor implements Processor {

    private HandlerContext context;

    HttpStaticResourcesProcessor(HandlerContext context) {

        this.context = context;
    }

    @Override
    public void process(HttpRequest request, HttpResponse response) throws Exception {
        String path = request.getPath().replaceFirst("/", "");
        String suffix = path.substring(path.lastIndexOf(".") + 1);
        File jarFile = context.getJarFile();
        byte[] bytes = FileUtils.readFileFromJar(jarFile, path);
        ContentTypeMapper mapper = context.getContentTypeMapper();
        String contentType = mapper.get(suffix);
        if (contentType != null) {
            response.addHeader("Content-Type", contentType);
        }
        OutputStream outputStream = response.getResponseOutputStream();
        outputStream.write(bytes);
    }
}
