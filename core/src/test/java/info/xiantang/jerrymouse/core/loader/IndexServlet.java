package info.xiantang.jerrymouse.core.loader;

import info.xiantang.jerrymouse.http.servlet.Request;
import info.xiantang.jerrymouse.http.servlet.Response;
import info.xiantang.jerrymouse.http.servlet.Servlet;

import java.io.IOException;
import java.io.OutputStream;

class IndexServlet implements Servlet {
    @Override
    public void service(Request request, Response response) throws IOException {
        OutputStream outputStream = response.getResponseOutputStream();
        outputStream.write("index".getBytes());

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        return obj instanceof IndexServlet;
    }
}