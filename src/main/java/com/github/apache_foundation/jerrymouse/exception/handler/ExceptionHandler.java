package com.github.apache_foundation.jerrymouse.exception.handler;

import com.github.apache_foundation.jerrymouse.exception.RequestInvalidException;
import com.github.apache_foundation.jerrymouse.network.wrapper.SocketWrapper;

import javax.servlet.ServletException;
import java.io.IOException;
/**
 * @Author: xiantang
 * @Date: 2019/4/17 14:45
 */
public class ExceptionHandler {
    public void handle(ServletException e, SocketWrapper socketWrapper) {
        try {
            if (e instanceof RequestInvalidException) {
                socketWrapper.close();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}
