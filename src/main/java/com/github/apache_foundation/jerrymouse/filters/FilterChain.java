package com.github.apache_foundation.jerrymouse.filters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * Created By CaiTieZhu on 2019/4/22
 */
public interface FilterChain {

    void doFilter(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException;
}
