package com.github.apachefoundation.jerrymouse.container.valve;

import com.github.apachefoundation.jerrymouse.http.HttpRequest;
import com.github.apachefoundation.jerrymouse.http.HttpResponse;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @Author: xiantang
 * @Date: 2019/5/24 19:45
 */
public interface Valve {
    public String getInfo();

    public void invoke(HttpRequest request, HttpResponse response, ValveContext valveContext) throws ServletException, IOException;
}
