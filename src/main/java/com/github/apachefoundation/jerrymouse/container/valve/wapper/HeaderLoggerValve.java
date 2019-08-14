package com.github.apachefoundation.jerrymouse.container.valve.wapper;

import com.github.apachefoundation.jerrymouse.container.Contained;
import com.github.apachefoundation.jerrymouse.container.Container;
import com.github.apachefoundation.jerrymouse.container.valve.Valve;
import com.github.apachefoundation.jerrymouse.container.valve.ValveContext;
import com.github.apachefoundation.jerrymouse.http.HttpRequest;
import com.github.apachefoundation.jerrymouse.http.HttpResponse;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * @Author: xiantang
 * @Date: 2019/5/28 20:23
 */
public class HeaderLoggerValve implements Valve, Contained {
    private Logger logger = Logger.getLogger(HeaderLoggerValve.class);

    @Override
    public Container getContainer() {
        return null;
    }

    @Override
    public void setContainer(Container container) {

    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public void invoke(HttpRequest request, HttpResponse response, ValveContext valveContext) throws Exception {
        valveContext.invokeNext(request, response);
        logger.info("HeaderLoggerValve");

    }
}
