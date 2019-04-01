package info.xiantang.basic.servlet;


import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * 使用serverSocket 建立与浏览器的链接 获取请求协议
 */
public class Service01 {
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
            System.out.println("一个客户端建立了链接----");
            // 获取请求协议
            InputStream is = client.getInputStream();
            byte[] data = new byte[1024 * 1024];
            int len = is.read(data);
            String requestInfo = new String(data, 0, len);
            StringBuilder content = new StringBuilder();
            content.append("<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>登陆</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<form method=\"post\" action=\"http://localhost:8888/login.html\">\n" +
                    "    用户名:\n" +
                    "    <input type=\"text\" name=\"uname\" id = \"uname\">\n" +
                    "    密码:\n" +
                    "    <input type=\"password\" name=\"pwd\" id=\"pwd\">\n" +
                    "    <input type=\"submit\" value=\"登陆\">\n" +
                    "\n" +
                    "</form>\n" +
                    "</body>\n" +
                    "</html>");

            int size = content.toString().getBytes().length;
            StringBuilder responseInfo = new StringBuilder();
            String blank = " ";
            String CRLF = "\r\n";
            // 返回响应协议
            // 状态行
            /*
            /login.html HTTP/1.1
             */
            responseInfo.append("HTTP/1.1").append(blank);
            responseInfo.append(200).append(blank);
            responseInfo.append("OK").append(CRLF);
            // 响应头
            /*
            Host: localhost:8888
            Connection: keep-alive
            Content-Length: 20
            Cache-Control: max-age=0
            Origin: http://localhost:63342
            Upgrade-Insecure-Requests: 1
            Content-Type: application/x-www-form-urlencoded
            User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36
            Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,
             */
            responseInfo.append("Date:").append(new Date()).append(CRLF);
            responseInfo.append("Server:").append("X Server/0.0.1;charset=UTF-8").append(CRLF);
            responseInfo.append("Content-Type:text/html").append(CRLF);
            responseInfo.append("Content-length:").append(size).append(CRLF);
            responseInfo.append(CRLF);
            // 正文
            responseInfo.append(content.toString());
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            bw.write(responseInfo.toString());
            bw.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Service01 server = new Service01();
        server.start();

    }
}
