package org.aaa.orm.entity.identifiable;

/**
 * Created by alexandremasanes on 21/10/2017.
 */
public abstract class IdGenerator {

    protected static IdGenerator instance;

    protected static void setId(IdentifiedByIdEntity entity, long id) {
        entity.setId(id);
    }
    /*
    protected void init() {
        IdentifiedByIdEntity.setIdGenerator(this);
    }*/

    protected abstract long generate(Class<? extends IdentifiedByIdEntity> entityClass);

    public static void injectNextId(IdentifiedByIdEntity entity) {
        setId(entity, instance.generate(entity.getClass()));
    }
}
