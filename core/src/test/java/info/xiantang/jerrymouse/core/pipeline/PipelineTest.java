package info.xiantang.jerrymouse.core.pipeline;

import info.xiantang.jerrymouse.core.handler.ServletContext;
import info.xiantang.jerrymouse.http.core.HttpRequest;
import info.xiantang.jerrymouse.http.core.HttpResponse;
import info.xiantang.jerrymouse.http.session.Cookies;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.HashMap;
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
        InBoundPipeline rootInBoundPipeline = new RootInBoundPipeline(context);
        InBoundPipeline testInBoundPipeline = new TestInBoundPipeline(context);
        rootInBoundPipeline.setNext(testInBoundPipeline);
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
        context.setInboundPipeline(rootInBoundPipeline);
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

    @Test
    public void testSessionInboundPipeline() {
        ServletContext context = ServletContext.emptyContext();
        InBoundPipeline rootInBoundPipeline = new RootInBoundPipeline(context);
        InBoundPipeline sessionInBoundPipeline = new SessionInBoundPipeline(context);
        rootInBoundPipeline.setNext(sessionInBoundPipeline);
        context.setInboundPipeline(rootInBoundPipeline);
        HttpRequest request = HttpRequest.newBuilder()
                .setMethod("POST")
                .setHttpVersion("HTTP/1.1")
                .setHeaders(new HashMap<>())
                .setBody("logname=aaaa&logpass=aaaa")
                .setPath("/login")
                .build();
        HttpResponse response = new HttpResponse(ByteBuffer.allocate(1024));
        rootInBoundPipeline.doHandle(request, response);

        String header = response.getHeader("Set-Cookie");
        assertNotNull(header);
    }

    @Test
    public void testRepeatRequestSessionInboundPipeline() {
        ServletContext context = ServletContext.emptyContext();
        InBoundPipeline rootInBoundPipeline = new RootInBoundPipeline(context);
        InBoundPipeline sessionInBoundPipeline = new SessionInBoundPipeline(context);
        rootInBoundPipeline.setNext(sessionInBoundPipeline);
        context.setInboundPipeline(rootInBoundPipeline);
        HttpRequest request = HttpRequest.newBuilder()
                .setMethod("POST")
                .setHttpVersion("HTTP/1.1")
                .setHeaders(new HashMap<>())
                .setBody("logname=aaaa&logpass=aaaa")
                .setPath("/login")
                .build();
        HttpResponse response = new HttpResponse(ByteBuffer.allocate(1024));
        rootInBoundPipeline.doHandle(request, response);

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
        rootInBoundPipeline.doHandle(request2, response2);

        String header2 = response2.getHeader("Set-Cookie");
        assertNull(header2);

    }

}
