package info.xiantang.basic.core;

import info.xiantang.basic.http.Request;
import info.xiantang.basic.http.Response;

import java.io.IOException;

public interface Servlet {
    void service(Request request, Response response) throws IOException;

}
