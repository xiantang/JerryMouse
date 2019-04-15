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
        writer.write("<body>aaaaa</body>");
        writer.flush();


    }

}
