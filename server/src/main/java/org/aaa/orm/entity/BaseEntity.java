package org.aaa.orm.entity;

import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by alexandremasanes on 23/09/2017.
 */
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = -8207979840833251610L;

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("Object#hashCode must be overriden !");
    }

    @Override
    public boolean equals(Object that) {
        throw new UnsupportedOperationException("Object#equals must be overriden !");
    }

    public static class BusinessException extends RuntimeException {}

    protected static void check(boolean bool) {
        if(!bool)
            throw new BusinessException();
    }

    protected static <T extends BaseEntity> T requireNonNull(T entity) {
        return Objects.requireNonNull(entity);
    }

    protected static <T extends BaseEntity> void requireNonNull(T... entities) {
        for(BaseEntity entity : entities)
            if(entity == null)
                throw new NullPointerException();
    }

    @SuppressWarnings("unchecked")
    public static Class<? extends BaseEntity> resolvedClass(BaseEntity entity) {
        return entity instanceof HibernateProxy ? (Class<? extends BaseEntity>)entity.getClass().getSuperclass() : entity.getClass();
    }
}
