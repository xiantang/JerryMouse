package com.github.apachefoundation.jerrymouse.lifecycle;

import java.util.EventObject;

/**
 * @Author: xiantang
 * @Date: 2019/5/30 20:28
 */
public class LifecycleEvent extends EventObject {
    private Object data = null;
    private Lifecycle lifecycle = null;
    private String type = null;
    public LifecycleEvent(Lifecycle lifecycle, String type, Object data) {
        super(lifecycle);
        this.data = data;
        this.lifecycle = lifecycle;
        this.type = type;
    }

    public LifecycleEvent(Lifecycle lifecycle, String type) {
        this(lifecycle, type, null);
    }
    public Object getData() {
        return data;
    }

    public Lifecycle getLifecycle() {
        return lifecycle;
    }

    public String getType() {
        return type;
    }


}
