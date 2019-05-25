package com.github.apachefoundation.jerrymouse.processor;

import com.github.apachefoundation.jerrymouse.context.WebApp;
import com.github.apachefoundation.jerrymouse.http.HttpRequest;
import com.github.apachefoundation.jerrymouse.http.HttpResponse;
import com.github.apachefoundation.jerrymouse.http.RequestFacade;
import com.github.apachefoundation.jerrymouse.http.ResponseFacade;
import sun.misc.Request;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * @Author: xiantang
 * @Date: 2019/5/22 16:07
 */
public class ServletProcessor {


    public void process(HttpRequest request, HttpResponse response) throws ServletException, IOException {
        Servlet servlet = null;
        servlet = (HttpServlet) WebApp.getServletFromUrl(request.getRequestURI());
        HttpServletRequest requestFacade = new RequestFacade(request);
        HttpServletResponse responseFacade = new ResponseFacade(response);
        if (servlet != null) {
            servlet.service(requestFacade, responseFacade);
        } else {
            servlet = (HttpServlet) WebApp.getServletFromUrl("404");
            servlet.service(requestFacade, responseFacade);
        }
    }
}
