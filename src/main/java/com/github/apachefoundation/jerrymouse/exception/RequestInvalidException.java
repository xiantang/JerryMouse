package com.github.apachefoundation.jerrymouse.exception;

import com.github.apachefoundation.jerrymouse.enumeration.HttpStatus;

/**
 * @Author: xiantang
 * @Date: 2019/4/17 14:45
 */
public class RequestInvalidException extends Exception {
    private HttpStatus status;

    public RequestInvalidException(HttpStatus status){
        this.status = status;
    }

}
