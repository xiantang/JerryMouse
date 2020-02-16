package info.xiantang.jerrymouse2.sample;

import info.xiantang.jerrymouse2.http.servlet.Request;
import info.xiantang.jerrymouse2.http.servlet.Response;
import info.xiantang.jerrymouse2.http.servlet.Servlet;

public class IndexServlet implements Servlet {
    @Override
    public void service(Request request, Response response) {
        response.setBody("/");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        return obj instanceof IndexServlet;
    }
}
