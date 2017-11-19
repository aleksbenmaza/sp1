package org.aaa.orm.entry;

import javax.persistence.Embedded;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by alexandremasanes on 09/09/2017.
 */
public abstract class BaseEntry<K extends Serializable, V extends Serializable>
implements Map.Entry<K, V>, Serializable {

    private static final long serialVersionUID = -5200365562667884271L;

    @Embedded
    private V value;

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        V oldValue;

        oldValue = this.value;

        this.value = value;

        return oldValue;
    }
}