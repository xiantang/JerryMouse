package info.xiantang.jerrymouse.http.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: xiantang
 * @Date: 2020/4/26 22:35
 */
public class Cookies {
    private Map<String, String> map = new HashMap<>();
    public void put(String key, String value) {
        map.put(key, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cookies cookies = (Cookies) o;
        return Objects.equals(map, cookies.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }

    public String get(String key) {
        return map.get(key);
    }
}
