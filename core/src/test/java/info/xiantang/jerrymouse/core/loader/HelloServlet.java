package info.xiantang.jerrymouse.core.loader;

import info.xiantang.jerrymouse.http.servlet.Request;
import info.xiantang.jerrymouse.http.servlet.Response;
import info.xiantang.jerrymouse.http.servlet.Servlet;

public class HelloServlet implements Servlet {

    @Override
    public void service(Request request, Response response) {
        response.setBody("hello");
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        return obj instanceof HelloServlet;
    }


}