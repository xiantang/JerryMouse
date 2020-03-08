package info.xiantang.jerrymouse.sample;

import info.xiantang.jerrymouse.http.servlet.Request;
import info.xiantang.jerrymouse.http.servlet.Response;
import info.xiantang.jerrymouse.http.servlet.Servlet;

class HelloServlet implements Servlet {


    @Override
    public void service(Request request, Response response) {
        NeedToLoad needToLoad = new NeedToLoad();
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