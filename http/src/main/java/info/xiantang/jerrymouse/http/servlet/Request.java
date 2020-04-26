package info.xiantang.jerrymouse.http.servlet;

import java.util.Map;

public interface Request {
    String getMethod();

    Map<String, String> getHeaders();

    String getPath();

    String getBody();
}
