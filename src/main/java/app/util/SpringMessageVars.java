package app.util;

/**
 * Created by alexandremasanes on 18/04/2017.
 */
public class SpringMessageVars {

    public static final char DEFAULT_DELIMITER = ',';

    private char delimiter;

    private Object[] values;

    public SpringMessageVars(char delimiter, Object... values) {
        this.delimiter = delimiter;
        this.values    = values;
    }

    public SpringMessageVars(Object... values) {
        this(DEFAULT_DELIMITER, values);
    }

    @Override
    public String toString() {
        StringBuilder str;
        
        str = new StringBuilder();

        for(Object value : values)
            str.append(value).append(delimiter);

        if(str.length() != 0)
            str.deleteCharAt(str.lastIndexOf(Character.toString(delimiter)));

        return str.toString();
    }
}
