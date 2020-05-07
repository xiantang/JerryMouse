package info.xiantang.jerrymouse.core.pipeline;

import info.xiantang.jerrymouse.core.handler.ServletContext;
import info.xiantang.jerrymouse.core.server.Context;
import info.xiantang.jerrymouse.core.server.HttpServer;
import info.xiantang.jerrymouse.core.utils.NetUtils;
import info.xiantang.jerrymouse.http.core.HttpRequest;
import info.xiantang.jerrymouse.http.core.HttpResponse;
import info.xiantang.jerrymouse.http.session.Cookies;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @Author: xiantang
 * @Date: 2020/4/26 22:48
 */
public class PipelineTest {
    @Test
    public void testRootInBoundPipeline() {
        ServletContext context = ServletContext.emptyContext();
        BoundPipeline rootBoundPipeline = new RootBoundPipeline(context);
        BoundPipeline testBoundPipeline = new TestBoundPipeline(context);
        rootBoundPipeline.setNext(testBoundPipeline);
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
        context.setInboundPipeline(rootBoundPipeline);
        context.getInboundPipeline().doHandle(request, response);
        Map<String, String> headers1 = new HashMap<>();
        headers1.put("Sec-Fetch-Dest", "image");
        headers1.put("Connection", "Keep-Alive");
        headers1.put("User-Agent", "Apache-HttpClient/4.5.3 (Java/1.8.0_232)");
        headers1.put("Accept-Encoding", "gzip,deflate");
        HttpRequest request1 = HttpRequest.newBuilder()
                .setMethod("POST")
                .setHttpVersion("HTTP/1.1")
                .setHeaders(headers1)
                .setPath("/favicon.ico")
                .build();
        assertEquals(request1, request);
    }

    private BoundPipeline createInBoundPipeline() {
        ServletContext context = ServletContext.emptyContext();
        BoundPipeline rootBoundPipeline = new RootBoundPipeline(context);
        BoundPipeline sessionBoundPipeline = new SessionInBoundPipeline(context);
        rootBoundPipeline.setNext(sessionBoundPipeline);
        context.setInboundPipeline(rootBoundPipeline);
        return rootBoundPipeline;
    }

    @Test
    public void testSessionInboundPipeline() {
        BoundPipeline rootBoundPipeline = createInBoundPipeline();
        HttpRequest request = HttpRequest.newBuilder()
                .setMethod("POST")
                .setHttpVersion("HTTP/1.1")
                .setHeaders(new HashMap<>())
                .setBody("logname=aaaa&logpass=aaaa")
                .setPath("/login")
                .build();
        HttpResponse response = new HttpResponse(ByteBuffer.allocate(1024));
        rootBoundPipeline.doHandle(request, response);
        String header = response.getHeader("Set-Cookie");
        assertNotNull(header);
    }

    @Test
    public void testRepeatRequestSessionInboundPipeline() {
        BoundPipeline rootBoundPipeline = createInBoundPipeline();
        HttpRequest request = HttpRequest.newBuilder()
                .setMethod("POST")
                .setHttpVersion("HTTP/1.1")
                .setHeaders(new HashMap<>())
                .setBody("logname=aaaa&logpass=aaaa")
                .setPath("/login")
                .build();
        HttpResponse response = new HttpResponse(ByteBuffer.allocate(1024));
        rootBoundPipeline.doHandle(request, response);
        String header = response.getHeader("Set-Cookie");
        assertNotNull(header);
        Cookies cookies = new Cookies();
        cookies.put("SESSION_ID", header.replace("SESSION_ID=", ""));
        HttpRequest request2 = HttpRequest.newBuilder()
                .setMethod("POST")
                .setHttpVersion("HTTP/1.1")
                .setHeaders(new HashMap<>())
                .setBody("logname=aaaa&logpass=aaaa")
                .setCookies(cookies)
                .setPath("/login")
                .build();
        HttpResponse response2 = new HttpResponse(ByteBuffer.allocate(1024));
        rootBoundPipeline.doHandle(request2, response2);

        String header2 = response2.getHeader("Set-Cookie");
        assertNull(header2);

    }


    @Test
    public void testContentLengthOutBound() throws Exception {
        int availablePort = NetUtils.getAvailablePort();
        HttpServer httpSever = new HttpServer("/tmp/static");
        List<Context> contexts = httpSever.getContexts();
        Context context = contexts.get(0);
        context.setPort(availablePort);
        context.init();
        context.start();
        ServletContext servletContext = context.getServletContext();
        BoundPipeline rootBoundPipeline = new RootBoundPipeline(servletContext);
        BoundPipeline contentOutBoundPipeline = new TestContentOutBoundPipeline(servletContext);
        rootBoundPipeline.setNext(contentOutBoundPipeline);
        servletContext.setOutboundPipeline(rootBoundPipeline);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://localhost:" + availablePort + "/hello");
        CloseableHttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        String responseStr = EntityUtils.toString(entity);
        assertEquals("hel", responseStr);
    }
}
