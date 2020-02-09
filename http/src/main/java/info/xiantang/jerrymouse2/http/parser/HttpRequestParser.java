package info.xiantang.jerrymouse2.http.parser;

import info.xiantang.jerrymouse2.http.exception.RequestParseException;
import info.xiantang.jerrymouse2.http.http.HttpRequest;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestParser {


    private final HttpRequest.Builder requestBuilder;
    private final byte[] rawRequest;
    private final byte SPACE = ' ';
    private final byte CR = '\r';
    private final byte LF = '\n';
    private final byte[] CRLF = new byte[]{CR, LF};
    private final byte COLON = ':';
    private final byte[] COLON_SPACE = new byte[]{COLON, SPACE};
    private int currentIndex = 0;
    private int preIndex = 0;


    public HttpRequestParser(byte[] rawRequest, HttpRequest.Builder requestBuilder) {
        this.rawRequest = rawRequest;
        this.requestBuilder = requestBuilder;
    }

    /**
     * can parse GET/POST http request
     * @return
     * @throws RequestParseException
     */
    public HttpRequest parse() throws RequestParseException {
        parseRequestLine();
        parseHeaders();
        parseBody();
        return requestBuilder.build();
    }

    private void parseBody() throws RequestParseException {
        // request not have body
        if (currentIndex >= rawRequest.length) {
            return;
        }
        String body = parseByTwoLinkedBytes(CRLF);
        requestBuilder.setBody(body);

        Map<String, String> headers = requestBuilder.getHeaders();
        // TODO parse charset from content-type
        if (headers.get("Content-Type") != null) {
            Map<String, String> parameters = parseParameters(body);
            requestBuilder.setParameters(parameters);
        }

    }

    private Map<String, String> parseParameters(String rawParameters) {
        String[] parameterArray = rawParameters.split("&");
        Map<String, String> parameters = requestBuilder.getParameters();
        for (String s : parameterArray) {
            String[] kv = s.split("=");
            parameters.put(kv[0], kv[1]);
        }
        return parameters;
    }


    private void parseRequestLine() throws RequestParseException {
        String method = parseBySpace();
        String pathWithParameters = parseBySpace();
        String httpVersion = parseByTwoLinkedBytes(CRLF);
        String path = splitPath(pathWithParameters);
        Map<String, String> parameters = splitParameters(pathWithParameters);
        Map<String, String> oldParameters = requestBuilder.getParameters();
        oldParameters.putAll(parameters);

        requestBuilder.setParameters(oldParameters);
        requestBuilder.setPath(path);
        requestBuilder.setMethod(method);
        requestBuilder.setHttpVersion(httpVersion);
    }

    private String splitPath(String pathWithParameters) {
        return pathWithParameters.split("\\?")[0];
    }

    private Map<String, String> splitParameters(String pathWithParameters) {
        String[] split = pathWithParameters.split("\\?");
        if (split.length > 1) {
            return parseParameters(split[1]);
        } else {
            return new HashMap<>();
        }
    }

    private void parseHeaders() throws RequestParseException {
        Map<String, String> headers = new HashMap<>();

        while (true) {
            if (!parseHeader(headers) || currentIndex > rawRequest.length) break;
        }
        requestBuilder.setHeaders(headers);
    }

    private String parseBySpace() throws RequestParseException {
        while (SPACE != rawRequest[currentIndex]) {
            currentIndex += 1;
            if (currentIndex >= rawRequest.length) {
                throw new RequestParseException("parse method out of bound");
            }
        }
        String result = new String(rawRequest, preIndex, currentIndex - preIndex);

        preIndex = currentIndex + 1;
        currentIndex = preIndex;

        return result;
    }

    private String parseByTwoLinkedBytes(byte[] bytes) throws RequestParseException {
        while (bytes[0] != rawRequest[currentIndex] && bytes[1] != rawRequest[currentIndex + 1]) {
            currentIndex += 1;
            if (currentIndex >= rawRequest.length) {
                throw new RequestParseException("parse method out of bound");
            }
        }

        String result = new String(rawRequest, preIndex, currentIndex - preIndex);

        preIndex = currentIndex + 2;
        currentIndex = preIndex;
        return result;
    }

    private boolean parseHeader(Map<String, String> headers) throws RequestParseException {
        String key = parseByTwoLinkedBytes(COLON_SPACE);
        String value = parseByTwoLinkedBytes(CRLF);
        headers.put(key, value);
        if (rawRequest[currentIndex] == CR && rawRequest[currentIndex + 1] == LF) {
            preIndex = currentIndex + 2;
            currentIndex = preIndex;
            return false;
        } else {
            return true;
        }
    }

}
