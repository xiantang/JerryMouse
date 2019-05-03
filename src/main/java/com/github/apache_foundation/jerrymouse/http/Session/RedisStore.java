package com.github.apache_foundation.jerrymouse.http.Session;

import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpSession;

public class RedisStore implements Store{
    private String host;
    private Jedis jedis;


    public RedisStore(String host) {
        this.host = host;
        this.jedis = new Jedis(host);
    }


    @Override
    public void load() {

    }

    @Override
    public void save() {

    }
}
