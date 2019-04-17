package info.xiantang.core;

import info.xiantang.core.network.endpoint.Endpoint;

/**
 * @Author: xiantang
 * @Date: 2019/4/17 14:45
 */
public class BootStrap {

    /**
     * 服务器启动入口
     */
    public static void run() {

        String connector = "nio";
        Endpoint server = Endpoint.getInstance(connector);
        server.start(8080);
    }

    public static void main(String[] args) {
        BootStrap.run();
    }
}
