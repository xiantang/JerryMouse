package info.xiantang.jerrymouse2.http.core;

import info.xiantang.jerrymouse2.core.servlet.Response;

public class HttpResponse implements Response {
    private String body;


    public void setBody(String body) {
        this.body = body;
    }

    public byte[] toRawResponse() {
        String content = "HTTP/1.1 200 OK\r\n" +
                "Date: Mon, 27 Jul 2009 12:28:53 GMT\n" +
                "Server: Apache/2.2.14 (Win32)\n" +
                "Last-Modified: Wed, 22 Jul 2009 19:15:56 GMT\n" +
                "Content-Type: text/html\n" +
                "Connection: Closed\r\n\n" +
                body;

        return content.getBytes();
    }
}
