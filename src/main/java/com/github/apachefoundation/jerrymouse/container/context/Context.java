package com.github.apachefoundation.jerrymouse.container.context;

import com.github.apachefoundation.jerrymouse.container.Container;
import com.github.apachefoundation.jerrymouse.container.mapper.Mapper;
import com.github.apachefoundation.jerrymouse.container.wrapper.Wrapper;
import com.github.apachefoundation.jerrymouse.http.HttpRequest;

/**
 * @Author: xiantang
 * @Date: 2019/5/29 14:59
 */
public interface Context extends Container {
    void addServletMapping(String pattern, String name);

    Mapper findMapper();

    String findServletMapping(String uri);

    Container map(HttpRequest request, boolean b);

    void load();

    void reload();
}
