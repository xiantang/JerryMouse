package info.xiantang.jerrymouse2.sample;

import info.xiantang.jerrymouse2.http.servlet.Request;
import info.xiantang.jerrymouse2.http.servlet.Response;
import info.xiantang.jerrymouse2.http.servlet.Servlet;

class HelloServlet implements Servlet {


    @Override
    public void service(Request request, Response response) {
        NeedToLoad needToLoad = new NeedToLoad();
        System.out.println(Servlet.class.getClassLoader());
        System.out.println(needToLoad.getClass().getClassLoader());
        System.out.println("aaaaaa");
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        return obj instanceof HelloServlet;
    }


}