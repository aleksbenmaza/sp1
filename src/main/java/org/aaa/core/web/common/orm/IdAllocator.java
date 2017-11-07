package org.aaa.core.web.common.orm;

import org.aaa.orm.entity.identifiable.IdentifiedByIdEntity;

import org.hibernate.SessionFactory;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Table;

/**
 * Created by alexandremasanes on 31/10/2017.
 */

@Component
public class IdAllocator {

    @Autowired
    private SessionFactory sessionFactory;

    protected long allocate(
                                        Class<? extends IdentifiedByIdEntity> entityClass,
            @SuppressWarnings("unused") Object...                             dummyVal
    ) {
        String stm;
        Query<Long> query;

        stm = "SELECT FUNCTION('allocate_auto_increment', :tableName, :allocBatchSize)";

        query = sessionFactory.getCurrentSession().createQuery(stm);

        query.setParameter("tableName", entityClass.getAnnotation(Table.class).name());
        query.setParameter("allocBatchSize", IdGeneratorImpl.AUTO_INCREMENT_BATCH_SIZE);

        return query.uniqueResult();
    }
}