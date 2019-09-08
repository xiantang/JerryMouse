package user;


import com.github.apachefoundation.jerrymouse.http.HttpRequest;
import com.github.apachefoundation.jerrymouse.http.HttpResponse;
import com.github.apachefoundation.jerrymouse.servlet.HttpServlet;

import java.io.IOException;
import java.io.Writer;


public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpRequest req, HttpResponse resp) throws IOException {
        Writer writer = resp.getWriter();
        writer.write("hello world!!!!");
        writer.flush();
    }
}
