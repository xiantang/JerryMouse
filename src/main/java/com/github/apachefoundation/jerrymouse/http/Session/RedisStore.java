package com.github.apachefoundation.jerrymouse.http.Session;

import redis.clients.jedis.Jedis;

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
