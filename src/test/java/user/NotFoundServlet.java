package user;


import com.github.apachefoundation.jerrymouse.http.HttpRequest;
import com.github.apachefoundation.jerrymouse.http.HttpResponse;
import com.github.apachefoundation.jerrymouse.servlet.HttpServlet;

import java.io.IOException;
import java.io.Writer;

/**
 * @Author: xiantang
 * @Date: 2019/5/22 11:34
 */
public class NotFoundServlet extends HttpServlet {
    @Override
    protected void doGet(HttpRequest req, HttpResponse resp) throws IOException {
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
