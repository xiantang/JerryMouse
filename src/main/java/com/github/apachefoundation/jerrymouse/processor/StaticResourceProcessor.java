package com.github.apachefoundation.jerrymouse.processor;

import com.github.apachefoundation.jerrymouse.http.HttpRequest;
import com.github.apachefoundation.jerrymouse.http.HttpResponse;

import java.io.IOException;


/**
 * @Author: xiantang
 * @Date: 2019/5/21 23:37
 */
public class StaticResourceProcessor {
    public void process(HttpRequest request, HttpResponse response) {
        try {
            response.sendStaticResource();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
