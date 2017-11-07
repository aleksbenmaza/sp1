package org.aaa.core.web.common.orm;

import org.aaa.orm.entity.identifiable.IdGenerator;
import org.aaa.orm.entity.identifiable.IdentifiedByIdEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.Table;

import java.util.HashMap;
import java.util.stream.Stream;

/**
 * Created by alexandremasanes on 21/10/2017.
 */
@Component
public class IdGeneratorImpl extends IdGenerator {

    protected final static short AUTO_INCREMENT_BATCH_SIZE = 10;

    @Autowired
    private IdAllocator idAllocator;

    private final HashMap<Class<? extends IdentifiedByIdEntity>, Class<? extends  IdentifiedByIdEntity>[]> classesCache;

    private final HashMap<Class<? extends IdentifiedByIdEntity>, Long> autoIncrementValuesByEntity;

    private final HashMap<Class<? extends IdentifiedByIdEntity>, Short> idSupplyCountsByEntity;

    {
        classesCache                = new HashMap<>();
        autoIncrementValuesByEntity = new HashMap<>();
        idSupplyCountsByEntity      = new HashMap<>();
    }

    @Override
    @PostConstruct
    public void init() {
      super.init();
    }

    @Override
    protected void generateFor(IdentifiedByIdEntity entity) {
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
            return;
        long nextId;
        Class<? extends IdentifiedByIdEntity> entityBaseClass;

        entityBaseClass = resolveBaseEntityClass(entity);

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
        setId(entity, nextId);
    }

    @SuppressWarnings("unchecked") 
    private Class<? extends IdentifiedByIdEntity> resolveBaseEntityClass(IdentifiedByIdEntity entity) {
        Class<? extends IdentifiedByIdEntity> entityBaseClass;

        for(
            entityBaseClass = entity.getClass();
            entityBaseClass.getSuperclass().isAnnotationPresent(Table.class);
            entityBaseClass = (Class<? extends IdentifiedByIdEntity>) entityBaseClass.getSuperclass()
        );

        return entityBaseClass;
    }
}