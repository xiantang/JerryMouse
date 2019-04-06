package info.xiantang.core;

import info.xiantang.core.network.endpoint.Endpoint;


public class BootStrap {

    /**
     * 服务器启动入口
     */
    public static void run() {
        // TODO:NIO BIO AIO
        String connector = "nio";
        Endpoint server = Endpoint.getInstance(connector);
        server.start(8080);

    }

    public static void main(String[] args) {
        BootStrap.run();
    }
}
