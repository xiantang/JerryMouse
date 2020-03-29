package info.xiantang.jerrymouse.http.servlet;

import java.io.IOException;

public interface Servlet {
    void service(Request request, Response response) throws IOException;
}
