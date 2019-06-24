package com.github.apachefoundation.jerrymouse.lifecycle;

/**
 * @Author: xiantang
 * @Date: 2019/5/30 20:21
 */
public interface Lifecycle {
    public static final String START_EVENT = "start";

    public static final String STOP_EVENT = "stop";

    public void addLifecycleListener(LifecycleListener listener);

    public LifecycleListener[] findLifecycleListeners();

    public void removLifecycleListener(LifecycleListener listener);

    public void start() throws LifecycleException;

    public void stop() throws LifecycleException;

}
