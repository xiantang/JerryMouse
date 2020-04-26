package info.xiantang.jerrymouse.core.pipeline;

import info.xiantang.jerrymouse.http.core.HttpRequest;
import info.xiantang.jerrymouse.http.core.HttpResponse;

/**
 * @Author: xiantang
 * @Date: 2020/4/26 23:04
 */
public interface Pipeline {


    void doHandle(HttpRequest request, HttpResponse response);
}
