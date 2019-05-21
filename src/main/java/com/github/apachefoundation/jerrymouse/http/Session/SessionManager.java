package com.github.apachefoundation.jerrymouse.http.Session;

import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * session 池
 * 创建一个此类对象 并根据配置设置储存方式
 */
public class SessionManager {
    private Map<String, HttpSession> sessionMap;

    /**
     * 储存引擎 日后根据配置设置
     */
    private Store storeManager;

    public SessionManager() {
        sessionMap = new ConcurrentHashMap<>();
    }

    public HttpSession creatSession() {
        String sessionId = System.currentTimeMillis() + "" + Math.random();
        HttpSession session = new StandardSession(sessionId);
        sessionMap.put(sessionId, session);
        return session;
    }

    public void putSession(HttpSession httpSession) {
        sessionMap.put(httpSession.getId(), httpSession);
    }

    public HttpSession findSession(String sessionId) {
        return sessionMap.get(sessionId);
    }

    public HttpSession[] findSessions() {
        Collection<HttpSession> sessions = sessionMap.values();
        return sessions.toArray(new HttpSession[0]);
    }

    public void remove(String sessionId) {
        sessionMap.remove(sessionId);
    }

    public void remove(HttpSession httpSession) {
        sessionMap.remove(httpSession.getId());
    }

    public void setStore(Store storeManager) {
        this.storeManager = storeManager;
    }

    public HttpSession getSession(String id) {
        return sessionMap.get(id);
    }

    /**
     * 将 session 持久化
     */
    public void load() {
        storeManager.save();
        sessionMap.clear();
    }

    /**
     * 将 session 读到内存
     */
    public void unload() {
        storeManager.load();
    }
}
