package info.xiantang.core.exception;

import info.xiantang.core.enumeration.HttpStatus;

public class ServletException extends Exception {
    private HttpStatus status;
    public ServletException(HttpStatus status){
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
