package org.aaa.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alexandremasanes on 29/04/2017.
 */
public final class HandlerDescription {

    static class Entry<K,V> implements Map.Entry<K,V> {
        final K key;
        V value;


        /**
         * Creates new entry.
         */
        Entry(K k, V v) {
            value = v;
            key = k;
        }

        public final K getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }

        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        public final boolean equals(Object o) {
            if (!(o instanceof Map.Entry))
                return false;
            Map.Entry e = (Map.Entry)o;
            Object k1 = getKey();
            Object k2 = e.getKey();
            if (k1 == k2 || (k1 != null && k1.equals(k2))) {
                Object v1 = getValue();
                Object v2 = e.getValue();
                if (v1 == v2 || (v1 != null && v1.equals(v2)))
                    return true;
            }
            return false;
        }

        public final int hashCode() {
            return (key==null   ? 0 : key.hashCode()) ^
                    (value==null ? 0 : value.hashCode());
        }

        public final String toString() {
            return getKey() + "=" + getValue();
        }

        /**
         * This method is invoked whenever the value in an entry is
         * overwritten by an invocation of put(k,v) for a key k that's already
         * in the HashMap.
         */
        void recordAccess(HashMap<K,V> m) {
        }

        /**
         * This method is invoked whenever the entry is
         * removed from the table.
         */
        void recordRemoval(HashMap<K,V> m) {
        }
    }

    private List<Map.Entry<String, Annotation>> annotedParameters;

    private Method method;

    public HandlerDescription(Method method){
        this.method = method;
        annotedParameters = new ArrayList<>();
    }

    public List<Map.Entry<String, Annotation>> getAnnotedParameters() {
        return annotedParameters;
    }

    public void addAnnotedParameter(String key, Annotation annotation) {
        annotedParameters.add(new Entry<String, Annotation>(key, annotation));
    }

    public Method getMethod() {
        return method;
    }
}
