package info.xiantang.core.context;



import info.xiantang.core.entity.Entity;
import info.xiantang.core.entity.Mapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Author: xiantang
 * @Date: 2019/4/17 14:45
 */
public class WebContext {

    private Map<String, String> entityMap = new HashMap<>();
    private Map<String, String> mappingMap = new HashMap<>();
    public WebContext(List<Entity> entities, List<Mapping> mappings) {

        for (Entity entity : entities) {
            entityMap.put(entity.getName(), entity.getClz());
        }
        // 将map 的List 转换为map
        for (Mapping mapping : mappings) {
            for (String pattern : mapping.getPatterns()
            ) {
                mappingMap.put(pattern, mapping.getName());
            }

        }
    }

    /**
     * 通过URL去找到class
     * @param pattern
     * @return
     */
    public String getClz(String pattern) {
        return entityMap.get(mappingMap.get(pattern));
    }

}
