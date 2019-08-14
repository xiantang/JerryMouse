package user;


import com.github.apachefoundation.jerrymouse.http.HttpRequest;
import com.github.apachefoundation.jerrymouse.http.HttpResponse;
import com.github.apachefoundation.jerrymouse.servlet.HttpServlet;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.Writer;
/**
 * 实现了正版servlet接口的船新版本
 */
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpRequest req, HttpResponse resp) throws IOException {
        Writer writer = resp.getWriter();
        writer.write("hello world!!!!");
        writer.flush();
    }
}
