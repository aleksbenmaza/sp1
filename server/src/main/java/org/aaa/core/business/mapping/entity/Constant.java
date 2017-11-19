package org.aaa.core.business.mapping.entity;

import org.aaa.orm.entity.ImmutableEntity;
import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.annotations.Formula;

import javax.persistence.*;

/**
 * Created by alexandremasanes on 18/11/2017.
 */
@Entity
@Table(name = "__constants__")
public class Constant extends ImmutableEntity {

    private static final long serialVersionUID = -6468746661206680260L;

    @Id
    private String name;

    @Formula("COALESCE(numeric_value::VARCHAR, varchar_value)")
    private String value;

    public String getName() {
        return name;
    }


    public Object getValue() {
        return NumberUtils.isNumber(value) ? NumberUtils.createNumber(value) : value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Constant constant = (Constant) o;

        return name.equals(constant.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @SuppressWarnings("unused")
    protected Constant() {}
}
