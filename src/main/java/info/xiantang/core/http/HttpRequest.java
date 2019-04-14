package info.xiantang.core.http;

import info.xiantang.core.utils.SocketInputStream;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.SocketChannel;
import java.security.Principal;
import java.util.*;

public class HttpRequest implements HttpServletRequest {
    //http 头中的属性
    private String contentType;
    private int contentLength = -1;
    private boolean keepAlive = true;
    // http 请求方式 GET, POST等
    private String method;
    // url 后面跟着的查询字符串
    private String queryString;
    //请求的 url
    private String requestURL;
    //请求的 uri
    private String requestURI;
    //协议名 一般就是http/1.1
    private String protocol;
    //请求报文
    private String body;
    //服务器名
    private String serverName;
    //端口号
    private int serverPort;
    //cookies
    private ArrayList<Cookie> cookies = new ArrayList<Cookie>();
    //http header 字段
    private Map<String, String> headersMap;
    //http 请求参数
    private Map<String, List<String>> parametersMap;
    //是否已解析
    private boolean parsed = false;
    // 协议信息
    private String requestInfo;

    public HttpRequest(SocketChannel socketChannel) throws IOException {
        headersMap = new HashMap<>();
        parametersMap = new HashMap<>();
        serverName = "Xserver";
        serverPort = 8080;
        SocketInputStream socketInputStream = new SocketInputStream(socketChannel);
        socketInputStream.readRequestLine(this);
        while (socketInputStream.readHttpHead(this));
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

    public void setParameter(String key, String value) {
        if (!parametersMap.containsKey(key)) {
            parametersMap.put(key, new ArrayList<String>());
        }
        parametersMap.get(key).add(value);
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
    public Enumeration<String> getHeaderNames() {
        Vector<String> headerNames = new Vector<String>();
        headerNames.addAll(headersMap.keySet());
        return headerNames.elements();
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
    public String getParameter(String s) {
        s = s.toLowerCase();
        if (!parametersMap.containsKey(s)) return null;
        return parametersMap.get(s).get(0);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        Vector<String> headerNames = new Vector<String>();
        headerNames.addAll(parametersMap.keySet());
        return headerNames.elements();
    }

    @Override
    public String[] getParameterValues(String s) {
        return parametersMap.get(s).toArray(new String[0]);
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
