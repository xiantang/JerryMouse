package com.github.apachefoundation.jerrymouse.container.valve;

import com.github.apachefoundation.jerrymouse.container.Contained;
import com.github.apachefoundation.jerrymouse.container.Container;
import com.github.apachefoundation.jerrymouse.http.HttpRequest;
import com.github.apachefoundation.jerrymouse.http.HttpResponse;
import com.github.apachefoundation.jerrymouse.network.connector.nio.NioPoller;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
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
    public void invoke(HttpRequest request, HttpResponse response, ValveContext valveContext) throws ServletException, IOException {
        valveContext.invokeNext(request, response);
        logger.info("HeaderLoggerValve");

    }
}
