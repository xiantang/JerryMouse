package info.xiantang.jerrymouse2.http.parser;

import info.xiantang.jerrymouse2.http.HttpRequest;
import info.xiantang.jerrymouse2.http.exception.RequestParseException;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TestHttpRequestParser {
    @Test
    public void canParseGetRequest() throws RequestParseException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        String reqStr = "GET / HTTP/1.1\r\n" +
                "Host: localhost:9820\r\n" +
                "Connection: Keep-Alive\r\n" +
                "User-Agent: Apache-HttpClient/4.5.3 (Java/1.8.0_232)\r\n" +
                "Accept-Encoding: gzip,deflate\r\n" +
                "\r\n";

        HttpRequestParser parser = new HttpRequestParser(reqStr.getBytes(), requestBuilder);
        HttpRequest httpRequest = parser.parse();
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "localhost:9820");
        headers.put("Connection", "Keep-Alive");
        headers.put("User-Agent", "Apache-HttpClient/4.5.3 (Java/1.8.0_232)");
        headers.put("Accept-Encoding", "gzip,deflate");

        HttpRequest expect = HttpRequest.newBuilder()
                .setMethod("GET")
                .setHttpVersion("HTTP/1.1")
                .setHeaders(headers)
                .setUri("/")
                .build();
        assertEquals(expect, httpRequest);
    }

}
