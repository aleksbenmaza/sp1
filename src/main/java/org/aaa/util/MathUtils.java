package org.aaa.util;

import static java.lang.Math.log10;

/**
 * Created by alexandremasanes on 24/08/2017.
 */
public final class MathUtils {

    public static long digitCount(long number) {
        return (long) log10(number) + 1;
    }

    MathUtils() {}
}
