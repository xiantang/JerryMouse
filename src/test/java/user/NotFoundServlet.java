package user;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * @Author: xiantang
 * @Date: 2019/5/22 11:34
 */
public class NotFoundServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String errorMessage = "<html>" +
                "<p> HTTP/1.1 404 File Not Found</p>\r\n" +
                "<p> Content-Type: text/html</p>\r\n" +
                "\r\n" +
                "<h1>File Not Found</h1></html>";
        Writer writer = resp.getWriter();
        writer.write(errorMessage);
        writer.flush();

    }
}
