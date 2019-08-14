package com.github.apachefoundation.jerrymouse.http;

import com.github.apachefoundation.jerrymouse.exception.RequestInvalidException;
import com.github.apachefoundation.jerrymouse.utils.PropertyUtil;
import com.github.apachefoundation.jerrymouse.utils.SocketInputBuffer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.*;

import static com.github.apachefoundation.jerrymouse.constants.Constants.*;

/**
 * @Author: xiantang
 * @Date: 2019/4/17 14:45
 */
public class HttpRequest {

    private String contentType;
    private int contentLength = -1;
    private boolean keepAlive = true;
    private String method;
    private String queryString;
    private String requestURL;
    private String requestURI;
    private String protocol;
    private String body;
    private String serverName;
    private int serverPort;
    private ArrayList<Cookie> cookies = new ArrayList<Cookie>();
    private Map<String, String> headersMap;
    private Map<String, List<String>> parametersMap;
    private SocketInputBuffer inputBuffer;
    private String remoteAddr;



    public HttpRequest(SocketInputBuffer inputBuffer) throws IOException, RequestInvalidException {
        headersMap = new HashMap<>();
        parametersMap = new HashMap<>();
        serverName = PropertyUtil.getProperty("server.name");
        serverPort = Integer.parseInt(PropertyUtil.getProperty("server.port"));
        this.inputBuffer = inputBuffer;
    }




    
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> parametersMap0 = new HashMap<>();

        Set<String> keySet = parametersMap.keySet();
        for (String k : keySet){
            parametersMap0.put(k, parametersMap.get(k).toArray(new String[0]));
        }
        return parametersMap0;
    }

    public void setParameter(String key, String value) {
        if (!parametersMap.containsKey(key)) {
            parametersMap.put(key, new ArrayList<String>());
        }
        parametersMap.get(key).add(value);
    }

    
    public Enumeration<String> getHeaderNames() {
        Vector<String> headerNames = new Vector<String>();
        headerNames.addAll(headersMap.keySet());
        return headerNames.elements();
    }

    
    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    
    public String[] getParameterValues(String s) {
        return parametersMap.get(s).toArray(new String[0]);
    }

    //==================================================
    //==================================================

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

    public void setServerName(String name) {
        this.serverName = name;
    }

    public void setServerPort(int port) {
        this.serverPort = port;
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



    public void setHead(String key, String value) {
        headersMap.put(key, value);
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void addCookie(Cookie cookie) {
        cookies.add(cookie);
    }

    
    public String getAuthType() {
        return null;
    }

    
    public Cookie[] getCookies() {
        return (Cookie[]) cookies.toArray();
    }

    
    public long getDateHeader(String s) {
        return 0;
    }

    
    public String getHeader(String s) {
        return headersMap.get(s);
    }

    
    public Enumeration<String> getHeaders(String s) {
        return null;
    }



    
    public int getIntHeader(String s) {
        return Integer.parseInt(headersMap.get(s));
    }

    
    public String getMethod() {
        return method;
    }

    
    public String getPathInfo() {
        return null;
    }

    
    public String getPathTranslated() {
        return null;
    }

    
    public String getContextPath() {
        return null;
    }

    
    public String getQueryString() {
        return queryString;
    }

    
    public String getRemoteUser() {
        return null;
    }

    
    public boolean isUserInRole(String s) {
        return false;
    }

    
    public Principal getUserPrincipal() {
        return null;
    }

    
    public String getRequestedSessionId() {
        return null;
    }

    
    public String getRequestURI() {
        return requestURI;
    }

    
    public StringBuffer getRequestURL() {
        return new StringBuffer(requestURL);
    }

    
    public String getServletPath() {
        return null;
    }

    
    public HttpSession getSession(boolean b) {
        return null;
    }

    
    public HttpSession getSession() {

        return null;
    }

    
    public String changeSessionId() {
        return null;
    }

    
    public boolean isRequestedSessionIdValid() {
        return false;
    }

    
    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    
    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    
    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    
    public Object getAttribute(String s) {
        return null;
    }

    
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    
    public String getCharacterEncoding() {
        return null;
    }

    
    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {

    }

    
    public int getContentLength() {
        return contentLength;
    }

    
    public long getContentLengthLong() {
        return 0;
    }

    
    public String getContentType() {
        return contentType;
    }

    
    public String getParameter(String s) {
        return null;
    }

    
    public Enumeration<String> getParameterNames() {
        return null;
    }
    
    public String getProtocol() {
        return protocol;
    }

    
    public String getScheme() {
        return null;
    }

    
    public String getServerName() {
        return serverName;
    }

    
    public int getServerPort() {
        return serverPort;
    }

    
    public BufferedReader getReader() throws IOException {
        return null;
    }

    
    public String getRemoteHost() {
        return null;
    }

    
    public void setAttribute(String s, Object o) {

    }

    
    public void removeAttribute(String s) {

    }

    
    public Locale getLocale() {
        return null;
    }

    
    public Enumeration<Locale> getLocales() {
        return null;
    }

    
    public boolean isSecure() {
        return false;
    }
    
    public String getRealPath(String s) {
        return null;
    }

    
    public int getRemotePort() {
        return 0;
    }

    
    public String getLocalName() {
        return null;
    }

    
    public String getLocalAddr() {
        return null;
    }

    
    public int getLocalPort() {
        return 0;
    }
    
    public boolean isAsyncStarted() {
        return false;
    }

    public boolean isAsyncSupported() {
        return false;
    }
}
