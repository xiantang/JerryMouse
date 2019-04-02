package info.xiantang.basic.servlet;


import info.xiantang.basic.Http.Request;
import info.xiantang.basic.Http.Response;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 使用serverSocket 建立与浏览器的链接 获取请求协议
 */
public class Service {
    private ServerSocket serverSocket;
    // 启动
    public void start() {
        try {
            serverSocket = new ServerSocket(8888);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("服务器启动失败");
        }
        while (true) {
            receive();
        }


    }

    public void stop() {

    }

    public void receive() {
        try {
            Socket client = serverSocket.accept();
            Request request = new Request(client);
            Response response = new Response(client);
            Servlet servlet = WebApp.getServletFromUrl(request.getUrl());
            if (servlet != null) {
                servlet.service(request, response);
                response.pushToBrower(200);
            } else response.pushToBrower(404);




        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Service server = new Service();
        server.start();

    }
}
