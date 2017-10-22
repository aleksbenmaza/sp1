package org.aaa.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by alexandremasanes on 10/03/2017.
 */
public final class ObjectMapper {

    public Map<String, Object> map(Object object) {
        Map<String, Object> map;
        Object value;

        map = new HashMap<>();

        for (Field field : object.getClass().getDeclaredFields()) {

            field.setAccessible(true);
            try {
                value = field.get(object);
            } catch (IllegalAccessException e) {
                continue;
            }
            map.put(field.getName(), value);
        }

        return map;
    }

}
