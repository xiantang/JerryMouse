package com.github.apachefoundation.jerrymouse.network.endpoint;

import com.github.apachefoundation.jerrymouse.utils.StringUtils;

import java.lang.reflect.InvocationTargetException;
/**
 * @Author: xiantang
 * @Date: 2019/4/17 14:45
 */

public abstract class Endpoint {
    /**
     * 启动服务器
     * @param port
     */
    public abstract void start(int port);

    private volatile boolean running = true;


    public boolean isRunning() {
        return running;
    }

    /**
     * 关闭服务器
     */
    public abstract void close();

    /**
     * 根据传入的字符串获取相对应的实例
     * @param connector
     * @return
     */
    public static Endpoint getInstance(String connector) {
        StringBuilder sb = new StringBuilder();
        sb.append("com.github.apachefoundation.jerrymouse.network.endpoint")
                .append(".")
                .append(connector)
                .append(".")
                .append(StringUtils.capitalize(connector))
                .append("Endpoint");
        try {
            return (Endpoint)Class.forName(sb.toString()).getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException(connector);
    }


}
