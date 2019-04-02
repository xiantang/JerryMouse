package info.xiantang.basic.servlet;


import info.xiantang.basic.Http.Request;
import info.xiantang.basic.Http.Response;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 使用serverSocket 建立与浏览器的链接 获取请求协议
 */
public class Service {

    private final ExecutorService exec = Executors.newCachedThreadPool();
    private ServerSocket socket;
    public void start()  {

        try {
            socket = new ServerSocket(8888);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (!exec.isShutdown()) {
            try {
                final Socket client = socket.accept();
                exec.execute(new Dispatcher(client));
            } catch (IOException e) {
                if (!exec.isShutdown())
                    log("task submission rejected", e);
            }
        }
    }

    public void stop() {
        exec.shutdown();
    }

    private void log(String msg, Exception e) {
        Logger.getAnonymousLogger().log(Level.WARNING, msg, e);
    }


    public static void main(String[] args) {
        Service server = new Service();
        server.start();

    }
}
