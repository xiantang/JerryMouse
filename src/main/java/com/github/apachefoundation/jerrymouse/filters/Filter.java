package com.github.apachefoundation.jerrymouse.filters;

import com.github.apachefoundation.jerrymouse.http.HttpRequest;
import com.github.apachefoundation.jerrymouse.http.HttpResponse;

import java.io.UnsupportedEncodingException;

/**
 * Created By CaiTieZhu on 2019/4/22
 */
public interface Filter {

    void doFilter(HttpRequest request, HttpResponse response, FilterChain chain) throws UnsupportedEncodingException;

    void destroy();
}
