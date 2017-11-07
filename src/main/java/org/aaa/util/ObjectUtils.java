package org.aaa.util;

import java.util.Arrays;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by alexandremasanes on 01/03/2017.
 */
public final class ObjectUtils {

    public static <T> T ifNull(
            T value,
            T other
    ) {
        return value != null ? value : other;
    }

    public static <T> T ifNull(
            T           value,
            Supplier<T> supplier
    ) {
        return value != null ? value : supplier.get();
    }

    public static <T> T ifNull(
            T                   value,
            Function<Object, T> function,
            Object              functionParameter
    ) {
        return value != null ? value : function.apply(functionParameter);
    }

    public static <O, T> T ifNotNull(O instance, Function<? super O, ? extends T> method, T nullSubstition) {
        return instance == null ? nullSubstition : method.apply(instance);
    }

    public static <O, T> T ifNotNull(Object instance, Object[] params,  Function<Object[], ? extends T> method, T nullSubstition) {
        params = reversePush(instance, params);
        return instance == null ? nullSubstition : method.apply(params);
    }


    public static <O> void ifNotNull(O instance, Consumer<? super O> method) {
        if(instance != null)
            method.accept(instance);
    }

    public static void ifNotNull(Object instance, Object[] params,  Consumer<Object[]> method) {
        params = reversePush(instance, params);
        if(instance != null)
            method.accept(params);
    }

    public static void doIf(Runnable runnable, boolean bool) {
        if(bool)
            runnable.run();
    }

    private static <O> Object[] reversePush(O instance, Object[] params) {
        params = Arrays.copyOfRange(params, -1, params.length);
        params[0] = instance;
        return params;
    }

    ObjectUtils() {}
}
