package info.xiantang;

import redis.clients.jedis.Jedis;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost");

        HashMap<String, String> map = new HashMap<>();
        map.put("zhangqi", "nb");
        map.put("asw", "sb");

        jedis.hset("a", map);
        Map map1 = jedis.hgetAll("a");
        System.out.println(map1.get("zhangqi"));
        System.out.println(map1.get("asw"));


//        System.out.println(map.get("a"));
//        System.out.println(map.get("b"));
//        System.out.println(map.get("c"));
//

        System.out.println("删除后...");

//        jedis.hdel("hashTest", "a");
//        jedis.hdel("hashTest", "b");
//        Map map1 = jedis.hgetAll("hashTest");

//        System.out.println(map1.get("a"));
//        System.out.println(map1.get("b"));
//        System.out.println(map1.get("c"));
    }
}
