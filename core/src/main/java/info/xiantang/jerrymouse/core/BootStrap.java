package info.xiantang.jerrymouse.core;

import info.xiantang.jerrymouse.core.server.HttpServer;

/**
 * @Author: xiantang
 * @Date: 2020/4/25 20:57
 */
public class BootStrap {
    public static void main(String[] args) throws Exception {
        HttpServer httpServer = new HttpServer();
        httpServer.init();
        httpServer.start();
    }
}
