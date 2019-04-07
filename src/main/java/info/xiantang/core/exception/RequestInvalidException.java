package info.xiantang.core.exception;

import info.xiantang.core.enumeration.HttpStatus;

public class RequestInvalidException extends  ServletException{
    private static final HttpStatus status = HttpStatus.BAD_REQUEST;
    public RequestInvalidException() {
        super(status);
    }
}
