package info.xiantang.jerrymouse.core.loader;

import info.xiantang.jerrymouse.http.servlet.Request;
import info.xiantang.jerrymouse.http.servlet.Response;
import info.xiantang.jerrymouse.http.servlet.Servlet;

class IndexServlet implements Servlet {
    @Override
    public void service(Request request, Response response) {
        response.setBody("index");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        return obj instanceof IndexServlet;
    }
}