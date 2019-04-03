package info.xiantang.basic.servlet;

import info.xiantang.basic.Http.Request;
import info.xiantang.basic.Http.Response;

import java.io.IOException;
import java.net.Socket;

/**
 * 分发器
 */
public class Dispatcher implements Runnable {

    private Socket client;
    private Request request;
    private Response response;
    public Dispatcher(Socket client) {
        this.client = client;

        try {
            request = new Request(client);
            response = new Response(client);
        } catch (IOException e) {
            e.printStackTrace();
            release();
        }

    }

    @Override
    public void run() {

        // socket 编程如果是浏览器请求会发送一个空包
        if (request.isEmptypackage()) {
            release();
            return;
        }
        try {
            Servlet servlet = WebApp.getServletFromUrl(request.getUrl());

            if (servlet != null) {
                servlet.service(request, response);
                response.pushToBrower(200);
            } else response.pushToBrower(404);
        } catch (Exception e) {
            try {
                response.pushToBrower(500);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        release();

    }

    private void release() {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
