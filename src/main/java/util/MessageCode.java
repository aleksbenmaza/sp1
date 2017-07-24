package app.util;

/**
 * Created by alexandremasanes on 19/07/2017.
 */
public class MessageCode {

    private String value;

    public MessageCode() {
    }

    public MessageCode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
