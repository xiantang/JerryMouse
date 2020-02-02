package info.xiantang.jerrymouse2.http.parser;

import info.xiantang.jerrymouse2.http.HttpRequest;
import info.xiantang.jerrymouse2.http.exception.RequestParseException;

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


    public HttpRequest parse() throws RequestParseException {
        parseRequestLine();
        parseHeaders();
        return requestBuilder.build();
    }

    private void parseRequestLine() throws RequestParseException {
        String method = parseBySpace();
        requestBuilder.setMethod(method);
        String uri = parseBySpace();
        requestBuilder.setUri(uri);
        String httpVersion = parseByTwoLinkedBytes(CRLF);
        requestBuilder.setHttpVersion(httpVersion);

    }

    private void parseHeaders() throws RequestParseException {
        Map<String, String> headers = new HashMap<>();

        while (parseHeader(headers) && currentIndex <= rawRequest.length) {

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
