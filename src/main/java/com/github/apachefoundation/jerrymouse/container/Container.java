package com.github.apachefoundation.jerrymouse.container;

import com.github.apachefoundation.jerrymouse.container.loader.Loader;
import com.github.apachefoundation.jerrymouse.http.HttpRequest;
import com.github.apachefoundation.jerrymouse.http.HttpResponse;
import sun.misc.Request;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @Author: xiantang
 * @Date: 2019/5/24 19:52
 */
public interface Container {
    public void invoke(HttpRequest request, HttpResponse response) throws ServletException, IOException;

    public void addChild(Container container);

    public void removeChild(Container container);

    public Container findChild(String name);

    void setLoader(Loader loader);

    public Loader getLoader();

    public Container[] findChildren();
}
