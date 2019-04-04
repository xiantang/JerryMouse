package info.xiantang.basic.exception;

import info.xiantang.basic.enumeration.HttpStatus;

public class ServletException extends Exception {
    private HttpStatus status;
    public ServletException(HttpStatus status){
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
