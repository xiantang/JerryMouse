package com.github.apachefoundation.jerrymouse.exception.handler;

import com.github.apachefoundation.jerrymouse.exception.RequestInvalidException;
import com.github.apachefoundation.jerrymouse.network.wrapper.SocketWrapper;

import javax.servlet.ServletException;
import java.io.IOException;
/**
 * @Author: xiantang
 * @Date: 2019/4/17 14:45
 */
public class ExceptionHandler {
    public void handle(Exception e, SocketWrapper socketWrapper) {
        try {
            if (e instanceof RequestInvalidException) {
                socketWrapper.close();
            } else if (e instanceof IOException) {
                socketWrapper.close();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}
