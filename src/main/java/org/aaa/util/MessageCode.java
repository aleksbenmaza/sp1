package org.aaa.util;

/**
 * Created by alexandremasanes on 19/07/2017.
 */
public final class MessageCode {

    public enum Type {
        SUCCESS,
        INFO,
        WARNING,
        DANGER
    }

    private Type type;

    private String value;

    public MessageCode() {
        this.type    = Type.INFO;
        this.value   = "";
    }

    public MessageCode(Type type, String value) {
        this.type  = type;
        this.value = value;
    }

    public MessageCode(String value) {
        this(Type.INFO, value);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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
