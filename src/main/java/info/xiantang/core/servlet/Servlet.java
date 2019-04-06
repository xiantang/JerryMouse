package info.xiantang.core.servlet;

import info.xiantang.core.http.Request;
import info.xiantang.core.http.Response;

import java.io.IOException;

public interface Servlet {
    void service(Request request, Response response) throws IOException;
}
