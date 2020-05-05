package info.xiantang.jerrymouse.http.servlet;

import info.xiantang.jerrymouse.http.session.Session;

import java.util.Map;

public interface Request {
    String getMethod();

    Map<String, String> getHeaders();

    String getPath();

    String getBody();

    Session getSession();
}
