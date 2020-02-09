package info.xiantang.jerrymouse2.http.core;


import info.xiantang.jerrymouse2.core.servlet.Request;
import info.xiantang.jerrymouse2.core.servlet.Servlet;

public abstract class HttpServlet implements Servlet {
    public abstract void service(Request request, Request response);
}
