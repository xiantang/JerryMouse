package info.xiantang.core.user;

import info.xiantang.core.http.Request;
import info.xiantang.core.http.Response;
import info.xiantang.core.servlet.Servlet;

public class RegisterServlet implements Servlet {
    @Override
    public void service(Request request, Response response) {
        response.pirnt("<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>注册</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<form method=\"post\" action=\"http://localhost:8888/login\">\n" +
                "    用户名:\n"+request.getParameter("uname") +
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
