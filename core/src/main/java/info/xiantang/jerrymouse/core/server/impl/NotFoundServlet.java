package info.xiantang.jerrymouse.core.server.impl;

import info.xiantang.jerrymouse.http.servlet.Request;
import info.xiantang.jerrymouse.http.servlet.Response;
import info.xiantang.jerrymouse.http.servlet.Servlet;

import java.io.IOException;
import java.io.OutputStream;

public class NotFoundServlet implements Servlet {


    @Override
    public void service(Request request, Response response) throws IOException {
        OutputStream responseOutputStream = response.getResponseOutputStream();
        responseOutputStream.write("404-NOT-FOUND".getBytes());
    }
}
