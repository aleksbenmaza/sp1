package org.aaa.util;


import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by alexandremasanes on 01/03/2017.
 */
public final class CommonUtils {

    public static <T> T replaceIfNull(T value, T other){
        return value != null ? value : other;
    }

    public static <T> T replaceIfNull(T value, Supplier<T> supplier){
        return value != null ? value : supplier.get();
    }

    public static <T> T replaceIfNull(T value, Function<Object, T> function, Object functionParameter){
        return value != null ? value : function.apply(functionParameter);
    }

    CommonUtils() {

    }
}
