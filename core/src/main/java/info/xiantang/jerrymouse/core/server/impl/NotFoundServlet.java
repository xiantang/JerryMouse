package info.xiantang.jerrymouse.core.server.impl;

import info.xiantang.jerrymouse.http.servlet.Request;
import info.xiantang.jerrymouse.http.servlet.Response;
import info.xiantang.jerrymouse.http.servlet.Servlet;

public class NotFoundServlet implements Servlet {


    @Override
    public void service(Request request, Response response) {
        response.setBody("404-NOT-FOUND");
    }
}
