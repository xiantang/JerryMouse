package info.xiantang.jerrymouse.core.pipeline;

import info.xiantang.jerrymouse.core.handler.ServletContext;
import info.xiantang.jerrymouse.http.core.HttpRequest;
import info.xiantang.jerrymouse.http.core.HttpResponse;
import info.xiantang.jerrymouse.http.session.Cookies;
import info.xiantang.jerrymouse.http.session.Session;

import java.util.Map;
import java.util.UUID;

/**
 * @Author: xiantang
 * @Date: 2020/4/26 23:16
 */
public class SessionInBoundPipeline extends BoundPipeline {
    public SessionInBoundPipeline(ServletContext context) {
        super(context);
    }

    @Override
    public void doHandle(HttpRequest request, HttpResponse response) {
        ServletContext context = getContext();
        Map<String, Session> sessionMap = context.getSessionMap();
        Cookies cookies = request.getCookies();
        if (cookies == null) {
            cookies = new Cookies();
            request.setCookies(cookies);
        }
        String sessionId = cookies.get("SESSION_ID");
        Session session;
        if (sessionId == null || sessionMap.get(sessionId) == null) {
            sessionId = UUID.randomUUID().toString();
            session = new Session(sessionId);
            response.addHeader("Set-Cookie", "SESSION_ID="+sessionId);
        } else {
            session = sessionMap.get(sessionId);
        }
        sessionMap.put(sessionId, session);
        request.setSession(session);
    }
}
