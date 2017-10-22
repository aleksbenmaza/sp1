package org.aaa.orm.entity.identifiable;

/**
 * Created by alexandremasanes on 21/10/2017.
 */
public interface IdGenerator {

    default void init() {
        IdentifiedByIdEntity.setIdGenerator(this);
    }

    <T extends IdentifiedByIdEntity> long generateFor(T entity);
}
