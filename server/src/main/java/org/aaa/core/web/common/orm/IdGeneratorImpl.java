package org.aaa.core.web.common.orm;

import org.aaa.orm.entity.BaseEntity;
import org.aaa.orm.entity.identifiable.IdGenerator;
import org.aaa.orm.entity.identifiable.IdentifiableById;
import org.aaa.orm.entity.identifiable.IdentifiedByIdEntity;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.HashMap;
import java.util.stream.Stream;

/**
 * Created by alexandremasanes on 21/10/2017.
 */
@Component
public class IdGeneratorImpl extends IdGenerator implements IdentifierGenerator {

    protected final static short AUTO_INCREMENT_BATCH_SIZE = 10;

    @Autowired
    private IdAllocator idAllocator;

    {
        IdGenerator.instance = this;
    }

    private final HashMap<Class<? extends IdentifiedByIdEntity>, Class<? extends  IdentifiedByIdEntity>[]> classesCache;

    private final HashMap<Class<? extends IdentifiedByIdEntity>, Long> autoIncrementValuesByEntity;

    private final HashMap<Class<? extends IdentifiedByIdEntity>, Short> idSupplyCountsByEntity;

    {
        classesCache                = new HashMap<>();
        autoIncrementValuesByEntity = new HashMap<>();
        idSupplyCountsByEntity      = new HashMap<>();
    }
/*
    @Override
    @PostConstruct
    public void init() {
      super.init();
    }
*/
    @Override
    protected long generate(Class<? extends IdentifiedByIdEntity> entityClass) {
        System.err.println("called ?");
        if(
           Stream.of(
                Thread.currentThread()
                      .getStackTrace())
                      .anyMatch(
                              stack -> "newInstance".equals(
                                      stack.getMethodName()
                              )
                      )
        )
            return IdentifiableById.NULL_ID;

        long nextId;
        Class<? extends IdentifiedByIdEntity> entityBaseClass;

        entityBaseClass = resolved(entityClass);

        autoIncrementValuesByEntity.computeIfAbsent(entityBaseClass, idAllocator::allocate);

        idSupplyCountsByEntity.computeIfAbsent(entityBaseClass, k -> (short) 0);
        if(idSupplyCountsByEntity.get(entityBaseClass) != AUTO_INCREMENT_BATCH_SIZE)
            nextId = idSupplyCountsByEntity.computeIfPresent(
                    entityBaseClass,
                    (k, v) -> ++v
            ) + autoIncrementValuesByEntity.getOrDefault(
                    entityBaseClass, 
                    0L
                );
        else {
            nextId = autoIncrementValuesByEntity.computeIfPresent(entityBaseClass, idAllocator::allocate);
            idSupplyCountsByEntity.computeIfAbsent(entityBaseClass, k -> (short) 0);
        }
        System.err.println(nextId);
        return nextId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        if(o instanceof IdentifiedByIdEntity) {
            IdGenerator.injectNextId((IdentifiedByIdEntity) o);
            return ((IdentifiedByIdEntity) o).getId();
        }
        throw new BaseEntity.BusinessException();
    }

    @SuppressWarnings("unchecked")
    private Class<? extends IdentifiedByIdEntity> resolved(Class<? extends IdentifiedByIdEntity> entityClass) {
        Class<? extends IdentifiedByIdEntity> entityBaseClass;

        for(
            entityBaseClass = entityClass;
            entityBaseClass.getSuperclass().isAnnotationPresent(Table.class);
            entityBaseClass = (Class<? extends IdentifiedByIdEntity>) entityBaseClass.getSuperclass()
        );

        return entityBaseClass;
    }

    public IdGeneratorImpl() {}
}