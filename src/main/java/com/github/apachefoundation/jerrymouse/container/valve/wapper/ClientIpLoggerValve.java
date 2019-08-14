package com.github.apachefoundation.jerrymouse.container.valve.wapper;

import com.github.apachefoundation.jerrymouse.container.Contained;
import com.github.apachefoundation.jerrymouse.container.Container;
import com.github.apachefoundation.jerrymouse.container.valve.Valve;
import com.github.apachefoundation.jerrymouse.container.valve.ValveContext;
import com.github.apachefoundation.jerrymouse.http.HttpRequest;
import com.github.apachefoundation.jerrymouse.http.HttpResponse;
import org.apache.log4j.Logger;

/**
 * @Author: xiantang
 * @Date: 2019/5/25 21:21
 */
public class ClientIpLoggerValve implements Valve, Contained {
    private static Logger logger = Logger.getLogger(ClientIpLoggerValve.class);
    private Container container;


    @Override
    public String getInfo() {
        return null;
    }


    @Override
    public Container getContainer() {
        return container;
    }

    @Override
    public void setContainer(Container container) {
        this.container = container;
    }

    @Override
    public void invoke(HttpRequest request, HttpResponse response, ValveContext valveContext) throws Exception {
        valveContext.invokeNext(request, response);

        String remoteIp = request.getRemoteAddr();
        logger.info("Client IP Logger Valve " + remoteIp);

    }
}
