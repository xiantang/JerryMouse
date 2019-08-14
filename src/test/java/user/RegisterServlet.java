package user;

import com.github.apachefoundation.jerrymouse.http.HttpRequest;
import com.github.apachefoundation.jerrymouse.http.HttpResponse;
import com.github.apachefoundation.jerrymouse.servlet.HttpServlet;

import java.io.IOException;
import java.io.Writer;

/**
 * 实现了正版servlet接口的船新版本
 */
public class RegisterServlet extends HttpServlet {
    private final String CRLF = "\r\n";

    @Override
    protected void doGet(HttpRequest req, HttpResponse resp) throws IOException {
        Writer writer = resp.getWriter();
        new Sum();
        writer.write("<body>aaaaa</body>");
        writer.flush();
    }

}
