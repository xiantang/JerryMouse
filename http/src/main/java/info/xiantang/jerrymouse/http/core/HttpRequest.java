package info.xiantang.jerrymouse.http.core;

import info.xiantang.jerrymouse.http.servlet.Request;
import info.xiantang.jerrymouse.http.session.Cookies;
import info.xiantang.jerrymouse.http.session.Session;
import info.xiantang.jerrymouse.http.utils.EqualsUtils;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest implements Request {

    private final Map<String, String> parameters;
    private final String body;
    private String path;
    private String method;
    private String version;
    private Map<String, String> headers;
    private Cookies cookies;
    private Session session;


    public HttpRequest(String method, String version, Map<String, String> headers, String path, Map<String, String> parameters, String body, Cookies cookies) {
        this.method = method;
        this.version = version;
        this.headers = headers;
        this.path = path;
        this.parameters = parameters;
        this.body = body;
        this.cookies = cookies;
    }


    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public String getPath() {
        return path;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof HttpRequest)) {
            return false;
        }

        HttpRequest that = (HttpRequest) obj;
        try {
            return EqualsUtils.OneDepthContentEquals(this, that);
        } catch (IllegalAccessException e) {
            // TODO use logging
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String getBody() {
        return body;
    }

    @Override
    public Session getSession() {
        return session;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Cookies getCookies() {
        return cookies;
    }

    public void setCookies(Cookies cookies) {
        this.cookies = cookies;
    }


    public void setSession(Session session) {
        this.session = session;
    }

    public static class Builder {

        private String method;
        private String httpVersion;
        private Map<String, String> headers = new HashMap<>();
        private String path;
        private Map<String, String> parameters = new HashMap<>();
        private String body;
        private Cookies cookies;


        public HttpRequest build() {
            return new HttpRequest(method, httpVersion, headers, path, parameters, body, cookies);
        }

        public Map<String, String> getHeaders() {
            return headers;
        }

        public Map<String, String> getParameters() {
            return parameters;
        }


        public Builder setMethod(String method) {
            this.method = method;
            return this;
        }


        public Builder setHttpVersion(String version) {
            this.httpVersion = version;
            return this;
        }

        public Builder setHeaders(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }


        public Builder setPath(String path) {
            this.path = path;
            return this;
        }

        public Builder setParameters(Map<String, String> parameters) {
            this.parameters = parameters;
            return this;
        }

        public Builder setBody(String body) {
            this.body = body;
            return this;
        }

        public Builder setCookies(Cookies cookies) {
            this.cookies = cookies;
            return this;
        }
    }
}
