package org.aaa.orm.entity.identifiable;

/**
 * Created by alexandremasanes on 21/10/2017.
 */
public abstract class IdGenerator {

    protected static void setId(IdentifiedByIdEntity entity, long id) {
        entity.setId(id);
    }

    protected void init() {
        IdentifiedByIdEntity.setIdGenerator(this);
    }

    protected abstract void generateFor(IdentifiedByIdEntity entity);
}
