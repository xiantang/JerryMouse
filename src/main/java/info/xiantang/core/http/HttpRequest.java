package info.xiantang.core.http;

import info.xiantang.core.exception.RequestInvalidException;
import info.xiantang.core.utils.PropertyUtil;
import info.xiantang.core.utils.SocketInputStream;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.SocketChannel;
import java.security.Principal;
import java.util.*;

import static org.apache.log4j.NDC.peek;

/**
 * @Author: xiantang
 * @Date: 2019/4/17 14:45
 */
@Getter
@Setter
public class HttpRequest implements HttpServletRequest {

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
    private boolean parsed = false;
    private SocketInputStream socketInputStream;


    public HttpRequest(SocketInputStream socketInputStream) throws IOException, RequestInvalidException {
        headersMap = new HashMap<>();
        parametersMap = new HashMap<>();
        serverName = PropertyUtil.getProperty("server.name");
        serverPort = Integer.parseInt(PropertyUtil.getProperty("server.port"));
        this.socketInputStream = socketInputStream;
        parseFirstRequestLine();
        parseRequestHeaders();
    }

    /**
     * 解析头信息
     */
    private void parseRequestHeaders() throws IOException, RequestInvalidException {
        boolean flag = true;
        while (flag) {
            flag = parseRequestHeader();
        }
    }

    private boolean parseRequestHeader() throws RequestInvalidException, IOException {
        StringBuilder httpHeadBuffer = new StringBuilder(1024);

        if (!socketInputStream.stuffRequestHeaderBuffer(httpHeadBuffer)) {
            return false;
        }
        socketInputStream.stuffRequestLineBuffer(httpHeadBuffer);

        String httpHead = httpHeadBuffer.toString().trim();
        String[] kv = httpHead.split(":");
        String headKey = kv[0].trim().toLowerCase();
        String headValue = kv[1].trim();
        String contentType = "contentType";
        String contentLength = "contentLength";
        String connection = "Connection";
        String keepAlive = "keep-alive";
        String cookie = "Cookie";
        if (headKey.equals(contentType)) {

            setContentType(headValue);
        } else if (headKey.equals(contentLength)) {
            setContentLength(Integer.parseInt(headValue));
        }

        else if (headKey.equals(connection)) {
            if (!headValue.equals(keepAlive)) {
                setKeepAlive(false);
            }
        } else if (headKey.equals(cookie)) {
            headValue = headValue.replaceAll(" ", "");
            String[] cookiesStr = headValue.split(";");
            addCookie(new Cookie(cookiesStr[0], cookiesStr[1]));
        }

        setHead(headKey, headValue);
        return true;

    }

    /**
     * 解析第一条的信息
     * @throws IOException
     * @throws RequestInvalidException
     */
    private void parseFirstRequestLine() throws IOException, RequestInvalidException {
        StringBuilder requestLineBuffer = new StringBuilder(1024);
        socketInputStream.stuffRequestBuffer(requestLineBuffer);
        String requestLine = requestLineBuffer.toString();
        String method = requestLine.substring(0, requestLine.indexOf("/")).trim();
        setMethod(method);
        int startidx = requestLine.indexOf("/") + 1;
        int endurix = requestLine.indexOf("?");
        int endurlx = requestLine.indexOf("HTTP/");
        String requestURL = requestLine.substring(startidx, endurlx).trim();
        setRequestURL(requestURL);
        String protocol   = requestLine.substring(endurlx);
        setProtocol(protocol);
        if (endurix == -1) {
            setRequestURI(requestURL);
        } else {
            String requestURI = requestLine.substring(startidx, endurix).trim();
            setRequestURI(requestURI);
            String param      = requestLine.substring(endurix + 1, endurlx);
            setQueryString(param);
            // 剖析 url 参数
            String[] keyValues = param.split("&");
            for (String queryStr : keyValues) {
                String[] kv = queryStr.split("=");
                kv = Arrays.copyOf(kv, 2);
                String key = kv[0];
                String value = kv[1]==null?null:socketInputStream.decode(kv[1],"utf-8");
                setParameter(key, value);
            }
        }
    }

    @Override
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
            parametersMap.put(key, new ArrayList<>());
        }
        parametersMap.get(key).add(value);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        Vector<String> headerNames = new Vector<>();
        headerNames.addAll(headersMap.keySet());
        return headerNames.elements();
    }

    //==================================================

    public void setHead(String key, String value) { headersMap.put(key, value); }

    public void addCookie(Cookie cookie) {
        cookies.add(cookie);
    }

    @Override
    public String getAuthType() {
        return null;
    }

    @Override
    public Cookie[] getCookies() {
        return (Cookie[]) cookies.toArray();
    }

    @Override
    public long getDateHeader(String s) {
        return 0;
    }

    @Override
    public String getHeader(String s) {
        return headersMap.get(s);
    }

    @Override
    public Enumeration<String> getHeaders(String s) {
        return null;
    }



    @Override
    public int getIntHeader(String s) {
        return Integer.parseInt(headersMap.get(s));
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getPathInfo() {
        return null;
    }

    @Override
    public String getPathTranslated() {
        return null;
    }

    @Override
    public String getContextPath() {
        return null;
    }

    @Override
    public String getQueryString() {
        return queryString;
    }

    @Override
    public String getRemoteUser() {
        return null;
    }

    @Override
    public boolean isUserInRole(String s) {
        return false;
    }

    @Override
    public Principal getUserPrincipal() {
        return null;
    }

    @Override
    public String getRequestedSessionId() {
        return null;
    }

    @Override
    public String getRequestURI() {
        return requestURI;
    }

    @Override
    public StringBuffer getRequestURL() {
        return new StringBuffer(requestURL);
    }

    @Override
    public String getServletPath() {
        return null;
    }

    @Override
    public HttpSession getSession(boolean b) {
        return null;
    }

    @Override
    public HttpSession getSession() {
        return null;
    }

    @Override
    public String changeSessionId() {
        return null;
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    @Override
    public boolean authenticate(HttpServletResponse httpServletResponse) throws IOException, ServletException {
        return false;
    }

    @Override
    public void login(String s, String s1) throws ServletException {

    }

    @Override
    public void logout() throws ServletException {

    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        return null;
    }

    @Override
    public Part getPart(String s) throws IOException, ServletException {
        return null;
    }

    @Override
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> aClass) throws IOException, ServletException {
        return null;
    }

    @Override
    public Object getAttribute(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {

    }

    @Override
    public int getContentLength() {
        return contentLength;
    }

    @Override
    public long getContentLengthLong() {
        return 0;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return null;
    }



    @Override
    public String[] getParameterValues(String s) {
        return parametersMap.get(s).toArray(new String[0]);
    }


    @Override
    public String getParameter(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return null;
    }
    @Override
    public String getProtocol() {
        return protocol;
    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public String getServerName() {
        return serverName;
    }

    @Override
    public int getServerPort() {
        return serverPort;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return null;
    }

    @Override
    public String getRemoteAddr() {
        return null;
    }

    @Override
    public String getRemoteHost() {
        return null;
    }

    @Override
    public void setAttribute(String s, Object o) {

    }

    @Override
    public void removeAttribute(String s) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public Enumeration<Locale> getLocales() {
        return null;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String s) {
        return null;
    }

    @Override
    public String getRealPath(String s) {
        return null;
    }

    @Override
    public int getRemotePort() {
        return 0;
    }

    @Override
    public String getLocalName() {
        return null;
    }

    @Override
    public String getLocalAddr() {
        return null;
    }

    @Override
    public int getLocalPort() {
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        return null;
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        return null;
    }

    @Override
    public boolean isAsyncStarted() {
        return false;
    }

    @Override
    public boolean isAsyncSupported() {
        return false;
    }

    @Override
    public AsyncContext getAsyncContext() {
        return null;
    }

    @Override
    public DispatcherType getDispatcherType() {
        return null;
    }
}
