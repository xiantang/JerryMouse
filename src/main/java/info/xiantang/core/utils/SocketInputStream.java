package info.xiantang.core.utils;

import info.xiantang.core.exception.RequestInvalidException;
import info.xiantang.core.http.HttpRequest;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * 这个类专门负责读取和解析 http 协议
 * 至于在什么时候解析 还没有定
 */
public class SocketInputStream {
    private SocketChannel socketChannel;
    private final String httpHeadEnd = "\\r\\n\\r\\n";
    private static final int CR = (int) '\r';
    private static final int LF = (int) '\n';
    //将数据读到缓冲区
    private ByteBuffer buffer;
    private int pos = 0;
    private int count = 0;



    public SocketInputStream(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
        this.buffer = ByteBuffer.allocate(2048);
    }

    /**
     * 填充requestLineBuffer
     * @param requestBuffer
     * @throws RequestInvalidException
     * @throws IOException
     */
    public void stuffRequestLineBuffer(StringBuilder requestBuffer) throws RequestInvalidException, IOException {
        int ch = -1;
        int head = pos;
        do {
            ch = read();
            if (ch == -1) {
                // 通過在這裏
                throw new RequestInvalidException(); //丢弃空包
            }
            requestBuffer.append((char) ch);
        } while (ch != CR && ch != LF);
    }

    /*
     * 读取并解析 http 的第一行
     */
    public void readRequestLine(HttpRequest httpRequest) throws IOException, RequestInvalidException {
        StringBuilder requestLineBuffer = new StringBuilder(1024);
        while (peek() == CR || peek() == LF || peek() == ' ' )
            read();
        stuffRequestLineBuffer(requestLineBuffer);
        String requestLine = requestLineBuffer.toString();
        String method = requestLine.substring(0, requestLine.indexOf("/")).trim();
        httpRequest.setMethod(method);
        int startidx = requestLine.indexOf("/") + 1;
        int endurix = requestLine.indexOf("?");
        int endurlx = requestLine.indexOf("HTTP/");
        String requestURL = requestLine.substring(startidx, endurlx).trim();
        httpRequest.setRequestURL(requestURL);
        String protocol   = requestLine.substring(endurlx);
        httpRequest.setProtocol(protocol);
        if (endurix == -1) {
            httpRequest.setRequestURI(requestURL);
        } else {
            String requestURI = requestLine.substring(startidx, endurix).trim();
            httpRequest.setRequestURI(requestURI);
            String param      = requestLine.substring(endurix + 1, endurlx);
            httpRequest.setQueryString(param);
            // 剖析 url 参数
            String[] keyValues = param.split("&");
            for (String queryStr : keyValues) {
                String[] kv = queryStr.split("=");
                kv = Arrays.copyOf(kv, 2);
                String key = kv[0];
                String value = kv[1]==null?null:decode(kv[1],"utf-8");
                httpRequest.setParameter(key, value);
            }
        }
    }

    private String decode(String value, String enc) {
        try {
            return URLDecoder.decode(value, enc);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * 读取一个请求头
     */
    public boolean readHttpHead(HttpRequest httpRequest) throws IOException, RequestInvalidException {
        StringBuilder httpHeadBuffer = new StringBuilder(1024);
        int i = 0;
        while (peek() == CR || peek() == LF || peek() == ' '){
            read();

            i++;
        }
        if (i >= 2) return false;

        stuffRequestLineBuffer(httpHeadBuffer);

        String httpHead = httpHeadBuffer.toString().trim();
        String[] kv = httpHead.split(":");
        String headKey = kv[0].trim().toLowerCase();
        String headValue = kv[1].trim();

        if (headKey.equals("contentType"))
            httpRequest.setContentType(headValue);
        else if (headKey.equals("contentLength"))
            httpRequest.setContentLength(Integer.parseInt(headValue));
        else if (headKey.equals("Connection")) {
            if (!headValue.equals("keep-alive"))
                httpRequest.setKeepAlive(false);
        }
        else if (headKey.equals("Cookie")) {
            headValue = headValue.replaceAll(" ", "");
            String[] cookiesStr = headValue.split(";");
            httpRequest.addCookie(new Cookie(cookiesStr[0], cookiesStr[1]));
        }

        httpRequest.setHead(headKey, headValue);
        return true;
    }

    private void readHttpBody(HttpRequest httpRequest) throws IOException {
        int contentLength = httpRequest.getContentLength();
        StringBuilder httpBody = new StringBuilder();
        if (contentLength >= 0) {
            for (int i = 0; i < contentLength; i++) {
                int ch = read();
                if (ch == -1) continue; //丢弃空包
                httpBody.append(ch);
            }
            httpRequest.setBody(httpBody.toString());
        } else {
            //变长body 日后支持
        }
    }


    private int read() throws IOException {
        if (pos >= count) {
            fill();

            if (pos >= count) {

                return -1;
            }
        }
        return buffer.get(pos++) & 0xff;
    }

    private int peek() throws IOException {
        if (pos >= count) {
            fill();

            if (pos >= count) {
                return -1;
            }
        }

        return buffer.get(pos) & 0xff;
    }

    private void fill() throws IOException {
        pos = 0;
        count = 0;
        int nRead = socketChannel.read(buffer);
        if (nRead > 0) {
            count = nRead;
        }
    }
}
