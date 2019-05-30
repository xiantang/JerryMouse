package com.github.apachefoundation.jerrymouse.container.valve;

import com.github.apachefoundation.jerrymouse.http.HttpRequest;
import com.github.apachefoundation.jerrymouse.http.HttpResponse;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @Author: xiantang
 * @Date: 2019/5/24 20:08
 */
public interface ValveContext {
    void setStage(int stage);

    public String getInfo();

    public void invokeNext(HttpRequest request, HttpResponse response) throws ServletException, IOException;

    void set(Valve basic, Valve[] valves);
}
