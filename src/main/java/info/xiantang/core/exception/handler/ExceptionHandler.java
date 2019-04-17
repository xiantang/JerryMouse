package info.xiantang.core.exception.handler;

import info.xiantang.core.exception.RequestInvalidException;
import info.xiantang.core.network.wrapper.SocketWrapper;

import javax.servlet.ServletException;
import javax.xml.ws.Response;
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
