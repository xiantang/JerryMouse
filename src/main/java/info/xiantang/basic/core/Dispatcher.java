package info.xiantang.basic.core;


import info.xiantang.basic.exception.RequestInvalidException;
import info.xiantang.basic.exception.ServletException;
import info.xiantang.basic.http.Request;
import info.xiantang.basic.http.Response;

import java.io.IOException;
import java.net.Socket;

/**
 * 分发器
 * 获得请求的url并且分配给指定的servlet处理
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
        } catch (RequestInvalidException e) {
            // 發送的是空包 所以不做處理  也不打印錯誤棧
            release();
        }

    }

    @Override
    public void run() {

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
