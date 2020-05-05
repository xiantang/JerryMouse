package info.xiantang.jerrymouse.http.session;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: xiantang
 * @Date: 2020/4/26 23:30
 */
public class Session {
    private String sessionId;
    private Map<String, Object> mapper = new HashMap<>();

    public void put(String key, Object value) {
        mapper.put(key, value);
    }

    public Object get(String key) {
        return mapper.get(key);
    }

    public Session(String sessionId) {

        this.sessionId = sessionId;
    }
}
