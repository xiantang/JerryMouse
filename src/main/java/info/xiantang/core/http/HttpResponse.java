package info.xiantang.core.http;

import info.xiantang.core.utils.SocketOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.*;

public class HttpResponse implements HttpServletResponse {
    private SocketChannel socketChannel;
    //cookies
    private ArrayList<Cookie> cookies = new ArrayList<Cookie>();
    //http header 字段
    private Map<String, String> headersMap;
    //状态字
    private int status;
    //状态信息
    private String statusInfo;
    //错误信息
    private String errorInfo;
    //字符集
    private String characterEncoding;
    //报文长度
    private int contentLength;
    //报文正文
    private ByteBuffer bodyBuffer;

    private final String  BLANK = " ";
    private final String CRLF = "\r\n";

    public HttpResponse(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
        this.headersMap = new HashMap<>();
        this.bodyBuffer = ByteBuffer.allocate(1024);
    }

    @Override
    public void addCookie(Cookie cookie) {
        cookies.add(cookie);
    }

    @Override
    public boolean containsHeader(String s) {
        return headersMap.containsKey(s);
    }

    @Override
    public String encodeURL(String s) {
        return null;
    }

    @Override
    public String encodeRedirectURL(String s) {
        return null;
    }

    @Override
    public String encodeUrl(String s) {
        return null;
    }

    @Override
    public String encodeRedirectUrl(String s) {
        return null;
    }

    @Override
    public void sendError(int i, String s) throws IOException {
        status = i;
        errorInfo = s;
    }

    @Override
    public void sendError(int i) throws IOException {
        status = i;
    }

    @Override
    public void sendRedirect(String s) throws IOException {
        //重定向 日后实现
    }

    @Override
    public void setDateHeader(String s, long l) {
        setHeader("Date", String.valueOf(new Date(l)));
    }

    @Override
    public void addDateHeader(String s, long l) {
        addHeader("Date", String.valueOf(new Date(l)));
    }

    @Override
    public void setHeader(String s, String s1) {
        headersMap.put(s, s1);
    }

    @Override
    public void addHeader(String s, String s1) {
        if (!containsHeader(s))
            setHeader(s, s1);
    }

    @Override
    public void setIntHeader(String s, int i) {
        setHeader(s, i+"");
    }

    @Override
    public void addIntHeader(String s, int i) {
        addHeader(s, i+"");
    }

    @Override
    public void setStatus(int i) {
        status = i;
    }

    @Override
    public void setStatus(int i, String s) {
        status = i;
        statusInfo = s;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getHeader(String s) {
        if (containsHeader(s))
            return headersMap.get(s);
        return null;
    }

    @Override
    public Collection<String> getHeaders(String s) {
        if (containsHeader(s)) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(headersMap.get(s));
            return arrayList;
        }
        return null;
    }

    @Override
    public Collection<String> getHeaderNames() {
        return headersMap.keySet();
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(new SocketOutputStream(socketChannel, this));
    }

    @Override
    public void setCharacterEncoding(String s) {
        this.characterEncoding = s;
    }

    @Override
    public void setContentLength(int i) {
        contentLength = i;
    }

    @Override
    public void setContentLengthLong(long l) {

    }

    @Override
    public void setContentType(String s) {

    }

    @Override
    public void setBufferSize(int i) {

    }

    @Override
    public int getBufferSize() {
        return bodyBuffer.capacity();
    }

    @Override
    public void flushBuffer() throws IOException {

    }

    @Override
    public void resetBuffer() {

    }

    @Override
    public boolean isCommitted() {
        return false;
    }

    @Override
    public void reset() {

    }

    public ByteBuffer getBodyBuffer() {
        return bodyBuffer;
    }

    @Override
    public void setLocale(Locale locale) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder resp = new StringBuilder();
        resp.append("HTTP/1.1").append(BLANK);
        resp.append(status).append(BLANK);

        switch (status) {
            case 200:
                resp.append("OK").append(CRLF);
                break;
            case 404:
                resp.append("NOT FOUND").append(CRLF);
                break;
            case 505:
                resp.append("SEVER ERROR").append(CRLF);
                break;
            default:
                resp.append(errorInfo);
        }

        Set<String> keySet = headersMap.keySet();

        for (String key: keySet) {
            resp.append(key);
            resp.append(":");
            resp.append(headersMap.get(key));
            resp.append(CRLF);
        }
        resp.append(CRLF);
        return resp.toString();
    }
}
