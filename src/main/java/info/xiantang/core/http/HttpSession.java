package info.xiantang.core.http;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpSession {

    private String id;
    private Map<String, Object> attributes;
    private boolean isVaild;

    private Instant lastAccessed;

    public HttpSession(String id, boolean isVaild) {
        this.id = id;
        this.attributes = new ConcurrentHashMap<>();
        this.isVaild = isVaild;
        this.lastAccessed = Instant.now();
    }

    public String getId() {
        return id;
    }

    /**
     * 獲取最後使用的時間
     * @return
     */
    public Instant getLastAccessed() {
        return lastAccessed;
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
}
