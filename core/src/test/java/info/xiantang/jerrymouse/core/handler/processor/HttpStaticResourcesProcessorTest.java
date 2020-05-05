package info.xiantang.jerrymouse.core.handler.processor;

import com.google.common.primitives.Bytes;
import info.xiantang.jerrymouse.core.conf.Configuration;
import info.xiantang.jerrymouse.core.handler.ServletContext;
import info.xiantang.jerrymouse.core.server.Context;
import info.xiantang.jerrymouse.core.server.ServletWrapper;
import info.xiantang.jerrymouse.core.utils.FileUtils;
import info.xiantang.jerrymouse.core.utils.NetUtils;
import info.xiantang.jerrymouse.http.core.HttpRequest;
import info.xiantang.jerrymouse.http.core.HttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class HttpStaticResourcesProcessorTest {

    @Test
    public void testStaticResourcesProcessorCanHandle() throws Exception {
        ServletContext servletContext = ServletContext.contextWithMapperAndClassLoader(null,null,"sample.jar");
        Processor processor = new HttpStaticResourcesProcessor(servletContext);
        HttpResponse response = new HttpResponse(ByteBuffer.allocate(1024));
        Map<String, String> headers = new HashMap<>();
        headers.put("Sec-Fetch-Dest", "image");
        headers.put("Connection", "Keep-Alive");
        headers.put("User-Agent", "Apache-HttpClient/4.5.3 (Java/1.8.0_232)");
        headers.put("Accept-Encoding", "gzip,deflate");
        HttpRequest request = HttpRequest.newBuilder()
                .setMethod("GET")
                .setHttpVersion("HTTP/1.1")
                .setHeaders(headers)
                .setPath("/favicon.ico")
                .build();
        processor.process(request,response);
        byte[] actual = response.getBodyBytes();
        byte[] except = FileUtils.readBytes(new File("core/src/test/resources/favicon.ico"));
        assertArrayEquals(except, actual);
    }

    @Test
    public void testStaticResourcesProcessJsFile() throws Exception {
        ServletContext servletContext = ServletContext.contextWithMapperAndClassLoader(null,null,"sample.jar");
        Processor processor = new HttpStaticResourcesProcessor(servletContext);
        HttpResponse response = new HttpResponse(ByteBuffer.allocate(1024));
        Map<String, String> headers = new HashMap<>();
        headers.put("Sec-Fetch-Dest", "image");
        headers.put("Connection", "Keep-Alive");
        headers.put("User-Agent", "Apache-HttpClient/4.5.3 (Java/1.8.0_232)");
        headers.put("Accept-Encoding", "gzip,deflate");
        HttpRequest request = HttpRequest.newBuilder()
                .setMethod("GET")
                .setHttpVersion("HTTP/1.1")
                .setHeaders(headers)
                .setPath("/scripts/jquery.min.js")
                .build();
        processor.process(request,response);
        byte[] actual = response.getBodyBytes();
        assertEquals(92554, actual.length);
    }


    @Test
    public void testCanHandleStaticRequest() throws Exception {
        int availablePort = NetUtils.getAvailablePort();
        Map<String, ServletWrapper> mapper = new HashMap<>();
        Configuration configuration = new Configuration(availablePort, 3, mapper);
        Context context = new Context("sample.jar", configuration);
        context.start();
        HttpGet httpget = new HttpGet("http://localhost:" + availablePort + "/favicon.ico");
        httpget.setHeader("Sec-Fetch-Dest", "image");
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        InputStream content = entity.getContent();
        int read;
        List<Byte> bytes = new ArrayList<>();
        while ((read = content.read()) != -1) {
            byte read1 = (byte) read;
            bytes.add(read1);
        }
        byte[] actual = Bytes.toArray(bytes);
        byte[] except = FileUtils.readBytes(new File("core/src/test/resources/favicon.ico"));
        assertArrayEquals(except, actual);
    }

}
