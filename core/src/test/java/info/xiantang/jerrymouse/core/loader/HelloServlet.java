package info.xiantang.jerrymouse.core.loader;

import info.xiantang.jerrymouse.http.servlet.Request;
import info.xiantang.jerrymouse.http.servlet.Response;
import info.xiantang.jerrymouse.http.servlet.Servlet;

import java.io.IOException;
import java.io.OutputStream;

public class HelloServlet implements Servlet {

    @Override
    public void service(Request request, Response response) throws IOException {
        OutputStream outputStream = response.getResponseOutputStream();
        outputStream.write("hello".getBytes());
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        return obj instanceof HelloServlet;
    }


}