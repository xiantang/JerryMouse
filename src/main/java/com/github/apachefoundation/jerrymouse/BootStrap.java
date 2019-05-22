package com.github.apachefoundation.jerrymouse;

import com.github.apachefoundation.jerrymouse.network.endpoint.Endpoint;
import com.github.apachefoundation.jerrymouse.utils.PropertyUtil;

import static com.github.apachefoundation.jerrymouse.constants.Constants.WEB_ROOT;

/**
 * @Author: xiantang
 * @Date: 2019/4/17 14:45
 */
public class BootStrap {

    /**
     * 服务器启动入口
     */
    public static void run() {
        String connector = PropertyUtil.getProperty("server.connector");
        String port = PropertyUtil.getProperty("server.port");
        Endpoint server = Endpoint.getInstance(connector);
        server.start(Integer.parseInt(port));
    }

    public static void main(String[] args) {
        BootStrap.run();
    }
}
