package info.xiantang.core.context;



import info.xiantang.core.entity.Entity;
import info.xiantang.core.entity.Mapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebContext {
    private List<Entity> entities;
    private List<Mapping> mappings;
    // key --> servlet-name value --> servlet-class
    private Map<String, String> entityMap = new HashMap<>();
    private Map<String, String> mappingMap = new HashMap<>();
    public WebContext(List<Entity> entities, List<Mapping> mappings) {
        // 存储实体类
        this.entities = entities;
        // 存储server 和 url 的映射
        this.mappings = mappings;
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
