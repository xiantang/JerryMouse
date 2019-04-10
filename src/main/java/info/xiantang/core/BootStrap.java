package info.xiantang.core;

import info.xiantang.core.network.endpoint.Endpoint;


public class BootStrap {

    /**
     * 服务器启动入口
     */
    public static void run() {

        String connector = "nio";
        Endpoint server = Endpoint.getInstance(connector);
        server.start(8081);
    }

    public static void main(String[] args) {
        BootStrap.run();
    }
}
