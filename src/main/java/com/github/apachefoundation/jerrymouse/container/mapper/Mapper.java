package com.github.apachefoundation.jerrymouse.container.mapper;

import com.github.apachefoundation.jerrymouse.container.Container;
import com.github.apachefoundation.jerrymouse.http.HttpRequest;


/**
 * @Author: xiantang
 * @Date: 2019/5/29 14:45
 */
public interface Mapper  {
    /**
     * 返回与该映射器相关联的servlet容器实例
     */
    public Container getContainer();

    public void setContainer(Container container);

    public void setProtocol(String protocol);

    public String getProtocol();

    /**
     * 返回要处理某个特定请求的子容器的实例
     * @param request
     * @param update
     * @return
     */
    public Container map(HttpRequest request, boolean update);

}
