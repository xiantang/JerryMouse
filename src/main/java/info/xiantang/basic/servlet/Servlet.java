package info.xiantang.basic.servlet;

import info.xiantang.basic.Http.Request;
import info.xiantang.basic.Http.Response;

public interface Servlet {
    void service(Request request, Response response);

}
