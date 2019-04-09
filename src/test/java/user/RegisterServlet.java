package user;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * 实现了正版servlet接口的船新版本
 */
public class RegisterServlet extends HttpServlet {
    private final String CRLF = "\r\n";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Writer writer = resp.getWriter();
        writer.write("<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>注册</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<form method=\"post\" action=\"http://localhost:8888/login\">\n" +
                "    用户名:\n"+req.getParameter("name") +
                "    <input type=\"text\" name=\"uname\" id = \"uname\">\n" +
                "    密码:\n" +
                "    <input type=\"password\" name=\"pwd\" id=\"pwd\">\n" +
                "    <input type=\"submit\" value=\"登陆\">\n" +
                "\n" +
                "</form>\n" +
                "</body>\n" +
                "</html>");
        writer.flush();


    }

}
