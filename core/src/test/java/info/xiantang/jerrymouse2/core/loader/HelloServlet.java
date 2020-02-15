package info.xiantang.jerrymouse2.core.loader;

import info.xiantang.jerrymouse2.http.servlet.Request;
import info.xiantang.jerrymouse2.http.servlet.Response;
import info.xiantang.jerrymouse2.http.servlet.Servlet;

class HelloServlet implements Servlet {
    @Override
    public void service(Request request, Response response) {
        response.setBody("hello");
    }

    public static void main(String[] args) {

    }
}