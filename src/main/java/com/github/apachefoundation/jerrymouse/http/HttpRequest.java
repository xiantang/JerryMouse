package com.github.apachefoundation.jerrymouse.http;

import javax.servlet.http.Cookie;
import java.util.*;


/**
 * @Author: xiantang
 * @Date: 2019/4/17 14:45
 */
public class HttpRequest {

    private String method;
    private String requestURI;
    private ArrayList<Cookie> cookies = new ArrayList<>();
    private Map<String, String> headersMap;
    private Map<String, String> parametersMap;
    private String remoteAddr;
    private boolean keepAlive;
    private String contentType;
    private int contentLength;
    private String queryString;
    private String requestURL;
    private String protocol;


    public HttpRequest() {
        headersMap = new HashMap<>();
        parametersMap = new HashMap<>();
    }


    public void setParameter(String key, String value) {
        parametersMap.put(key, value);
    }


    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }


    public void setContentLength(int length) {
        this.contentLength = length;
    }

    public void setContentType(String type) {
        this.contentType = type;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }


    public void setHeader(String key, String value) {
        headersMap.put(key, value);
    }

    public String getHeader(String key) {
        return headersMap.get(key);
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public void addCookie(Cookie cookie) {
        cookies.add(cookie);
    }


    public String getMethod() {
        return method;
    }


    public String getRequestURI() {
        return requestURI;
    }


    public void setCharacterEncoding(String s) {

    }


    /**
     * 获取指定的参数
     * @param s
     * @return
     */
    public String getParameter(String s) {
        return parametersMap.get(s);
    }


}
