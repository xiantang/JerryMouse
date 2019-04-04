package info.xiantang.basic.user;

import info.xiantang.basic.http.Request;
import info.xiantang.basic.http.Response;
import info.xiantang.basic.core.Servlet;

import java.io.IOException;

public class LoginServlet implements Servlet {
    @Override
    public void service(Request request, Response response) throws IOException {
        response.pirnt("<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>登陆</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<form method=\"post\" action=\"http://localhost:8888/login\">\n" +
                "    用户名:\n" +
                "    <input type=\"text\" name=\"uname\" id = \"uname\">\n" +
                "    密码:\n" +
                "    <input type=\"password\" name=\"pwd\" id=\"pwd\">\n" +
                "    <input type=\"submit\" value=\"登陆\">\n" +
                "\n" +
                "</form>\n" +
                "</body>\n" +
                "</html>");
    }
}
