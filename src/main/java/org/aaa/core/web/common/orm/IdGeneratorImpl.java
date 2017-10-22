package org.aaa.core.web.common.orm;

import org.aaa.orm.entity.identifiable.IdGenerator;
import org.aaa.orm.entity.identifiable.IdentifiedByIdEntity;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.PersistenceContext;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;

/**
 * Created by alexandremasanes on 21/10/2017.
 */
//@Component
public class IdGeneratorImpl implements IdGenerator {

    private static short AUTO_INCREMENT_STEP = 10;

    @PersistenceContext
    private Session session;

    private final HashMap<Class<? extends IdentifiedByIdEntity>, Long> autoIncrementValuesByEntity;

    private final HashMap<Class<? extends IdentifiedByIdEntity>, Short> idSupplyCountsByEntity;

    {
        autoIncrementValuesByEntity = new HashMap<>();
        idSupplyCountsByEntity      = new HashMap<>();
    }

    @Override
    @PostConstruct
    public void init() {
    }

    @Override
    public <T extends IdentifiedByIdEntity> long generateFor(T entity) {

        return 0;
    }
}
