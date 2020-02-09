package info.xiantang.jerrymouse2.http.parser;

import info.xiantang.jerrymouse2.http.core.HttpRequest;
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
                .setPath("/")
                .build();
        assertEquals(expect, httpRequest);
    }


    @Test
    public void canParseGetWithQueryStringParameters() throws RequestParseException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        String reqStr = "GET /xiantang?org=growingio&year_list=1 HTTP/1.1\r\n" +
                "Host: github.com\r\n" +
                "Connection: keep-alive\r\n" +
                "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36\r\n" +
                "\r\n";
        HttpRequestParser parser = new HttpRequestParser(reqStr.getBytes(), requestBuilder);
        HttpRequest actual = parser.parse();
        Map<String, String> parameters = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "github.com");
        headers.put("Connection", "keep-alive");
        headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36");
        parameters.put("org", "growingio");
        parameters.put("year_list", "1");
        HttpRequest expected = HttpRequest.newBuilder()
                .setMethod("GET")
                .setHttpVersion("HTTP/1.1")
                .setHeaders(headers)
                .setPath("/xiantang")
                .setParameters(parameters)
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void canParsePostFormUrlEncodeRequest() throws RequestParseException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        String reqStr = "POST / HTTP/1.1\r\n" +
                "Content-Length: 31\r\n"+
                "Content-Type: application/x-www-form-urlencoded;charset=utf-8\r\n" +
                "\r\n" +
                "title=test&key1=1&key2=2&key3=3";
        HttpRequestParser parser = new HttpRequestParser(reqStr.getBytes(), requestBuilder);
        HttpRequest httpRequest = parser.parse();
        Map<String, String> headers = new HashMap<>();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("title", "test");
        parameters.put("key1", "1");
        parameters.put("key2", "2");
        parameters.put("key3", "3");
        headers.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.put("Content-Length", "31");

        HttpRequest expect = HttpRequest.newBuilder()
                .setMethod("POST")
                .setHttpVersion("HTTP/1.1")
                .setHeaders(headers)
                .setPath("/")
                .setParameters(parameters)
                .setBody("title=test&key1=1&key2=2&key3=3")
                .build();

        assertEquals(expect, httpRequest);
    }
}
