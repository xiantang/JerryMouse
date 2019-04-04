package info.xiantang.basic.exception;

import info.xiantang.basic.enumeration.HttpStatus;

public class RequestInvalidException extends  ServletException{
    private static final HttpStatus status = HttpStatus.BAD_REQUEST;
    public RequestInvalidException() {
        super(status);
    }
}
