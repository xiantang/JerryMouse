package com.github.apachefoundation.jerrymouse.container.valve;

import com.github.apachefoundation.jerrymouse.http.HttpRequest;
import com.github.apachefoundation.jerrymouse.http.HttpResponse;

/**
 * @Author: xiantang
 * @Date: 2019/5/24 19:45
 */
public interface Valve {
    String getInfo();

    void invoke(HttpRequest request, HttpResponse response, ValveContext valveContext) throws Exception;
}
