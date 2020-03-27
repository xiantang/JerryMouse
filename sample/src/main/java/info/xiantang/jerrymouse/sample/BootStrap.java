package info.xiantang.jerrymouse.sample;


import info.xiantang.jerrymouse.core.server.HttpServer;

public class BootStrap {
    public static void main(String[] args) throws Exception {
        HttpServer httpServer = new HttpServer();
        httpServer.init();
        httpServer.start();
    }
}
