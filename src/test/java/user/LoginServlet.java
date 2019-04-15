package user;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;

/**
 * 实现了正版servlet接口的船新版本
 */
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(req.getMethod());
        System.out.println(req.getRequestURI());
        System.out.println(req.getQueryString());
        Enumeration<String> headers = req.getHeaderNames();

        while (headers.hasMoreElements()) {
            String header = headers.nextElement();
            System.out.println(header);
            System.out.println(req.getHeader(header));
        }

        Writer writer = resp.getWriter();
        writer.write("aaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        writer.flush();
    }
}
