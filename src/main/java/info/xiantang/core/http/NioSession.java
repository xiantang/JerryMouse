package info.xiantang.core.http;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.time.Instant;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NioSession implements HttpSession {

    private String id;
    private Map<String, Object> attributes;
    private boolean isVaild;
    private Instant lastAccessed;
    private Instant creationTime;
    private int maxInactiveInterval;

    public NioSession(String id) {
        this.id = id;
        this.attributes = new ConcurrentHashMap<>();
        this.isVaild = true;
        this.lastAccessed = Instant.now();
        this.creationTime = Instant.now();
        this.maxInactiveInterval = 3600;
    }
    public String getId() {
        return id;
    }

    /**
     * 設置元素
     * @param key
     * @param value
     */
    public void setAttribute(String key, Object value) {
        if (isVaild) {
            this.lastAccessed = Instant.now();
            attributes.put(key, value);
        } else {
            throw new IllegalStateException("session has invalidated!");
        }

    }

    /**
     * 獲取session中的元素
     * @param key
     * @return
     */
    public Object getAttribute(String key) {
        if (isVaild) {
            this.lastAccessed = Instant.now();
            return attributes.get(key);
        } else {
            throw new IllegalArgumentException("session has invalidated!");
        }

    }
    @Override
    public long getCreationTime() {
        return creationTime.getEpochSecond();
    }

    /**
     * 獲取最後使用的時間
     * @return
     */
    @Override
    public long getLastAccessedTime() {
        return lastAccessed.getEpochSecond();
    }



    @Override
    public void setMaxInactiveInterval(int i) {
        maxInactiveInterval = i;
    }

    @Override
    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }


    /**
     * 获取session中的所有键
     * @return
     */
    @Override
    public Enumeration<String> getAttributeNames() {
        if (isVaild) {
            return Collections.enumeration(attributes.keySet());
        }
        throw new IllegalArgumentException("session has invalidated!");
    }

    

    @Override
    public void removeAttribute(String s) {
        if (isVaild) {
            attributes.remove(s);
        }
        throw new IllegalArgumentException("session has invalidated!");
    }


    /**
     * 清除session
     * 将状态置为false
     * 并且清除session内的数据
     */
    @Override
    public void invalidate() {
        isVaild = false;
        attributes.clear();

    }

    @Override
    public boolean isNew() {
        return false;
    }

    //--------------------------------------------------------------------------
    //--------------------------------未实现-------------------------------------
    //--------------------------------------------------------------------------

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    /**
     * @param s
     * @param o
     * @deprecated
     */
    @Override
    public void putValue(String s, Object o) {

    }

    /**
     * @param s
     * @deprecated
     */
    @Override
    public void removeValue(String s) {

    }
    /** 被弃用 暂时不实现
     * @param s
     * @deprecated
     */
    @Override
    @Deprecated
    public Object getValue(String s) {

        return null;
    }


    /**
     * @deprecated
     */
    @Override
    public String[] getValueNames() {
        return new String[0];
    }

    /**
     * @deprecated
     */
    @Override
    public HttpSessionContext getSessionContext() {
        return null;
    }
}
