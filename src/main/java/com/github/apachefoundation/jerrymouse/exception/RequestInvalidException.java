package com.github.apachefoundation.jerrymouse.exception;

import com.github.apachefoundation.jerrymouse.enumeration.HttpStatus;

import javax.servlet.ServletException;
/**
 * @Author: xiantang
 * @Date: 2019/4/17 14:45
 */
public class RequestInvalidException extends ServletException {
    private HttpStatus status;

    public RequestInvalidException(HttpStatus status){
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
