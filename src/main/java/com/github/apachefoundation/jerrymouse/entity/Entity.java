package com.github.apachefoundation.jerrymouse.entity;


/**
 * @Author: xiantang
 * @Date: 2019/4/17 14:45
 * 实体类
 * 主要是负责存储类名和类的路径
 * 用来反射
 */
public class Entity {
    private String name;
    private String clz;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClz() {
        return clz;
    }

    public void setClz(String clz) {
        this.clz = clz;
    }
}
