package com.github.apachefoundation.jerrymouse.container.pipeline;

import com.github.apachefoundation.jerrymouse.container.valve.Valve;
import com.github.apachefoundation.jerrymouse.http.HttpRequest;
import com.github.apachefoundation.jerrymouse.http.HttpResponse;

/**
 * 管道接口 负责设置基础阀和添加删除阀
 * @Author: xiantang
 * @Date: 2019/5/24 19:44
 */
public interface Pipeline {
    public Valve getBasic();

    public void setBasic(Valve valve);

    public void addValve(Valve valve);

    public void invoke(HttpRequest request, HttpResponse response);

    public void removeValve(Valve valve);

}
