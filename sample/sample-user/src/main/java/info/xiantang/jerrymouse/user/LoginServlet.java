package info.xiantang.jerrymouse.user;

import info.xiantang.jerrymouse.http.servlet.Request;
import info.xiantang.jerrymouse.http.servlet.Response;
import info.xiantang.jerrymouse.http.servlet.Servlet;
import info.xiantang.jerrymouse.http.session.Session;

import java.io.IOException;
import java.io.OutputStream;

public class LoginServlet implements Servlet {
    @Override
    public void service(Request request, Response response) throws IOException {

        if (request.getMethod().equals("POST")) {
            Session session = request.getSession();
            String body = request.getBody();
            String[] split = body.split("&");
            String name = split[0].split("=")[1];
            String pwd = split[0].split("=")[1];
            User user = new User(name, pwd);
            session.put("user", user);
        }
        Session session = request.getSession();
        Object user = session.get("user");
        OutputStream responseOutputStream = response.getResponseOutputStream();
        if (user == null) {
            String resource = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\" class=\"no-js\">\n" +
                    "<head>\n" +
                    "<meta charset=\"UTF-8\" />\n" +
                    "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"> \n" +
                    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"> \n" +
                    "<title>login</title>\n" +
                    "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/normalize.css\" />\n" +
                    "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/demo.css\" />\n" +
                    "<!--必要样式-->\n" +
                    "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/component.css\" />\n" +
                    "<!--[if IE]>\n" +
                    "<script src=\"js/html5.js\"></script>\n" +
                    "<![endif]-->\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "\t\t<div class=\"container demo-1\">\n" +
                    "\t\t\t<div class=\"content\">\n" +
                    "\t\t\t\t<div id=\"large-header\" class=\"large-header\">\n" +
                    "\t\t\t\t\t<canvas id=\"demo-canvas\"></canvas>\n" +
                    "\t\t\t\t\t<div class=\"logo_box\">\n" +
                    "\t\t\t\t\t\t<h3>欢迎你</h3>\n" +
                    "\t\t\t\t\t\t<form action=\"/\" name=\"f\" method=\"post\">\n" +
                    "\t\t\t\t\t\t\t<div class=\"input_outer\">\n" +
                    "\t\t\t\t\t\t\t\t<span class=\"u_user\"></span>\n" +
                    "\t\t\t\t\t\t\t\t<input name=\"logname\" class=\"text\" style=\"color: #FFFFFF !important\" type=\"text\" placeholder=\"请输入账户\">\n" +
                    "\t\t\t\t\t\t\t</div>\n" +
                    "\t\t\t\t\t\t\t<div class=\"input_outer\">\n" +
                    "\t\t\t\t\t\t\t\t<span class=\"us_uer\"></span>\n" +
                    "\t\t\t\t\t\t\t\t<input name=\"logpass\" class=\"text\" style=\"color: #FFFFFF !important; position:absolute; z-index:100;\"value=\"\" type=\"password\" placeholder=\"请输入密码\">\n" +
                    "\t\t\t\t\t\t\t</div>\n" +
                    "\t\t\t\t\t\t\t<div class=\"mb2\"><input class=\"act-but submit\" type=\"submit\" style=\"color: #FFFFFF\"></input></div>\n" +

                    "\t\t\t\t\t\t</form>\n" +
                    "\t\t\t\t\t</div>\n" +
                    "\t\t\t\t</div>\n" +
                    "\t\t\t</div>\n" +
                    "\t\t</div><!-- /container -->\n" +
                    "\t\t<script src=\"js/TweenLite.min.js\"></script>\n" +
                    "\t\t<script src=\"js/EasePack.min.js\"></script>\n" +
                    "\t\t<script src=\"js/rAF.js\"></script>\n" +
                    "\t\t<script src=\"js/demo-1.js\"></script>\n" +
                    "\t\t<script src=\"js/login.js\"></script>\n" +
                    "\t</body>\n" +
                    "</html>";
            responseOutputStream.write(resource.getBytes());
        } else {
            User user1 = (User) user;
            String resource = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\" class=\"no-js\">\n" +
                    "<head>\n" +
                    "<meta charset=\"UTF-8\" />\n" +
                    "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"> \n" +
                    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"> \n" +
                    "<title>login</title>\n" +
                    "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/normalize.css\" />\n" +
                    "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/demo.css\" />\n" +
                    "<!--必要样式-->\n" +
                    "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/component.css\" />\n" +
                    "<!--[if IE]>\n" +
                    "<script src=\"js/html5.js\"></script>\n" +
                    "<![endif]-->\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "\t\t<div class=\"container demo-1\">\n" +
                    "\t\t\t<div class=\"content\">\n" +
                    "\t\t\t\t<div id=\"large-header\" class=\"large-header\">\n" +
                    "\t\t\t\t\t<canvas id=\"demo-canvas\"></canvas>\n" +
                    "\t\t\t\t\t<div class=\"logo_box\">\n" +
                    "\t\t\t\t\t\t<h3>" + user1.getName() + "欢迎你</h3>\n" +
                    "\t\t\t\t\n" +
                    "\t\t\t\t\t</div>\n" +
                    "\t\t\t\t</div>\n" +
                    "\t\t\t</div>\n" +
                    "\t\t</div><!-- /container -->\n" +
                    "\t\t<script src=\"js/TweenLite.min.js\"></script>\n" +
                    "\t\t<script src=\"js/EasePack.min.js\"></script>\n" +
                    "\t\t<script src=\"js/rAF.js\"></script>\n" +
                    "\t\t<script src=\"js/demo-1.js\"></script>\n" +
                    "\t</body>\n" +
                    "</html>";
            responseOutputStream.write(resource.getBytes());
        }


    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        return obj instanceof LoginServlet;
    }
}
