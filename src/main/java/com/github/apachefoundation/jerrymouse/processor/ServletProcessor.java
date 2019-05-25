package com.github.apachefoundation.jerrymouse.processor;

import com.github.apachefoundation.jerrymouse.container.SimpleContainer;
import com.github.apachefoundation.jerrymouse.http.HttpRequest;
import com.github.apachefoundation.jerrymouse.http.HttpResponse;

import javax.servlet.ServletException;
import java.io.IOException;


/**
 * @Author: xiantang
 * @Date: 2019/5/22 16:07
 */
public class ServletProcessor {


    public void process(HttpRequest request, HttpResponse response) throws ServletException, IOException {
        SimpleContainer simpleContainer = new SimpleContainer();
        simpleContainer.invoke(request,response);
    }
}
