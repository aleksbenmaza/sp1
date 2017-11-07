package org.aaa.core.web.app.helper;

import static org.apache.commons.lang.ArrayUtils.add;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import static java.util.regex.Pattern.compile;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alexandremasanes on 22/08/2017.
 */

@Component
@SuppressWarnings("all")
public class Routes implements Map<String ,String> {

    private static final String PROPERTY_PREFIX = "routes";

    private static final Pattern pathVariablePattern;

    static {
        pathVariablePattern = compile("\\{(\\w+)\\}");
    }

    @Autowired
    private Properties properties;

    @Override
    public int size() {
        return properties.size();
    }

    @Override
    public boolean isEmpty() {
        return properties.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return properties.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return properties.containsValue(value);
    }

    @Override
    @Deprecated
    public String put(String key, String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public String remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public void putAll(Map<? extends String, ? extends String> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public void clear() {
        throw new UnsupportedOperationException();
    }


    @Override
    public Set<String> keySet() {
        return ImmutableSet.copyOf((Collection) properties.keySet());
    }


    @Override
    public Collection<String> values() {
        return ImmutableList.copyOf((Collection) properties.values());
    }


    @Override
    public Set<Entry<String, String>> entrySet() {
        return ImmutableSet.copyOf((Collection) properties.entrySet());
    }

    @Override
    public String get(Object key) {
        String fullKey;

        fullKey = PROPERTY_PREFIX + "." + key;

        return properties.getProperty(fullKey);
    }

    public String get(String key, Object param, Object... params) {
        add(params, 0, param);
        return replace(get(key), param, params);
    }

    private static String replace(String str, Object... values) {
        StringBuffer sb;
        Matcher      m;
        int          n;

        sb = new StringBuffer();
        m = pathVariablePattern.matcher(str);

        n = 0;

        while (m.find()) {
            m.appendReplacement(sb, "");
            sb.append(values[n++]);
        }

        m.appendTail(sb);

        return sb.toString();
    }
}