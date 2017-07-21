package app.util;


/**
 * Created by alexandremasanes on 01/03/2017.
 */
public final class CommonUtils {

    public static <T> T replaceIfNull(T value, T other){
        return value != null ? value : other;
    }

    CommonUtils() {

    }
}
