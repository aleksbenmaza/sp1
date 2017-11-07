package org.aaa.util;

import com.google.common.math.BigIntegerMath;

import java.math.BigInteger;
import java.math.RoundingMode;

import static java.lang.Math.log10;

/**
 * Created by alexandremasanes on 24/08/2017.
 */
public final class MathUtils {

    public static long digitCount(long number) {
        return (long) log10(number) + 1;
    }

    public static long digitCount(BigInteger number) {
        return BigIntegerMath.log10(number, RoundingMode.UNNECESSARY);
    }

    MathUtils() {}
}
