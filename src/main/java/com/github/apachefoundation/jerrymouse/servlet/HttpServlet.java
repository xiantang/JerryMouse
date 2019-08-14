package com.github.apachefoundation.jerrymouse.servlet;

import com.github.apachefoundation.jerrymouse.http.HttpRequest;
import com.github.apachefoundation.jerrymouse.http.HttpResponse;

import java.io.IOException;

public class HttpServlet {

    public void service(HttpRequest req, HttpResponse resp) throws IOException {
        doGet(req, resp);
    }

    protected void doGet(HttpRequest req, HttpResponse resp) throws IOException {

    }
}
