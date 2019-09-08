package com.github.apachefoundation.jerrymouse.container;

import com.github.apachefoundation.jerrymouse.container.loader.Loader;
import com.github.apachefoundation.jerrymouse.http.HttpRequest;
import com.github.apachefoundation.jerrymouse.http.HttpResponse;

/**
 * @Author: xiantang
 * @Date: 2019/5/24 19:52
 */
public interface Container {
    void invoke(HttpRequest request, HttpResponse response) throws Exception;

    void addChild(Container container);

    void removeChild(Container container);

    Container findChild(String name);

    void setLoader(Loader loader);

    Loader getLoader();

}
