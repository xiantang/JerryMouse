package info.xiantang.jerrymouse2.http;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Request {

    private String uri;
    private String method;
    private String version;
    private Map<String, String> headers;

    public Request(String method, String version, Map<String, String> headers, String uri) {
        this.method = method;
        this.version = version;
        this.headers = headers;
        this.uri = uri;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Request)) {
            return false;
        }

        Request that = (Request) obj;
        try {
            return getFieldMap(this).equals(getFieldMap(that));
        } catch (IllegalAccessException e) {
            //TODO log error
            e.printStackTrace();
        }
        return false;
    }

    private Map<String, Object> getFieldMap(Object obj) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();

        Map<String, Object> fieldMap = new HashMap<>();
        for (Field field : fields) {
            field.setAccessible(true);
            fieldMap.put(field.getName(), field.get(obj));
        }
        return fieldMap;
    }


    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        private String method;
        private String httpVersion;
        private Map<String, String> headers;
        private String uri;


        public Request build() {
            return new Request(method, httpVersion, headers, uri);
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


        public Builder setUri(String uri) {
            this.uri = uri;
            return this;
        }
    }
}
