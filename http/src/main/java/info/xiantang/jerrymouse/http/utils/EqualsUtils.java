package info.xiantang.jerrymouse.http.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class EqualsUtils {

    /**
     * compare two object with depth 1
     * @param firstObj
     * @param secondObj
     * @return
     * @throws IllegalAccessException
     */
    public static boolean OneDepthContentEquals(Object firstObj, Object secondObj) throws IllegalAccessException {
        return getFieldMap(firstObj).equals(getFieldMap(secondObj));
    }

    private static Map<String, Object> getFieldMap(Object obj) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();

        Map<String, Object> fieldMap = new HashMap<>();
        for (Field field : fields) {
            field.setAccessible(true);
            fieldMap.put(field.getName(), field.get(obj));
        }
        return fieldMap;
    }

}
