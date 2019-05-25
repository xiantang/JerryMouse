package com.github.apachefoundation.jerrymouse.container;

import javax.servlet.http.HttpServlet;

/**
 * @Author: xiantang
 * @Date: 2019/5/24 20:17
 */
public interface Wrapper extends Container {
    /**
     * 分配一个已经初始化的实例
     * @return
     */
    public HttpServlet allocate();

    public void load();
}
