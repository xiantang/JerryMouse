package com.github.apachefoundation.jerrymouse.http.Session;

public interface Store {
    /**
     * 将 session 从硬盘写回内存
     */
    void load();

    /**
     * 将 session 从内存写到硬盘
     */
    void save();
}
