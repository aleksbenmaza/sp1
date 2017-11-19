package org.aaa.orm.entry.onetoone;

import org.aaa.orm.entity.BaseEntity;
import org.aaa.orm.entry.BaseEntry;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.io.Serializable;

/**
 * Created by alexandremasanes on 09/09/2017.
 */

@Embeddable
public class Entry<K extends BaseEntity, V extends Serializable> extends BaseEntry<K, V> {

    private static final long serialVersionUID = -5992690016361004102L;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private K key;

    public Entry(K key) {
        this.key = key;
    }

    @Override
    public K getKey() {
        return key;
    }

    Entry() {}
}
