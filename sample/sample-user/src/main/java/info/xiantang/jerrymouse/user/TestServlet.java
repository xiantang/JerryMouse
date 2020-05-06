package info.xiantang.jerrymouse.user;

import info.xiantang.jerrymouse.http.servlet.Request;
import info.xiantang.jerrymouse.http.servlet.Response;
import info.xiantang.jerrymouse.http.servlet.Servlet;

import java.io.OutputStream;

public class TestServlet implements Servlet {
    @Override
    public void service(Request request, Response response) throws Exception {
        OutputStream responseOutputStream = response.getResponseOutputStream();
        responseOutputStream.write("xiantang".getBytes());
    }
}
