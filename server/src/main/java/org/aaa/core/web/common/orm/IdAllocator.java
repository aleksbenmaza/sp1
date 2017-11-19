package org.aaa.core.web.common.orm;

import org.aaa.orm.entity.identifiable.IdentifiedByIdEntity;
import org.aaa.util.ObjectUtils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;

import org.hibernate.type.LongType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.persistence.Table;

import java.util.Map;

/**
 * Created by alexandremasanes on 31/10/2017.
 */

@Component
public class IdAllocator {

   // @Value("#{${allocationBatchSizes}}")
    private Map<Class<? extends IdentifiedByIdEntity>, Short> allocationBatchSizes;

    @Autowired
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    protected long allocate(
                                        Class<? extends IdentifiedByIdEntity> entityClass,
            @SuppressWarnings("unused") Object...                             unused
    ) {
        Session session;
        String stm;
        NativeQuery<Long> query;
        long nextId;

        session = sessionFactory.openSession();

        stm = "SELECT __allocate_auto_increment(:tableName, :allocBatchSize) AS next_id";

        query = sessionFactory.getCurrentSession()
                              .createNativeQuery(stm);

        query.setParameter("tableName", entityClass.getAnnotation(Table.class).name());
        query.setParameter("allocBatchSize", IdGeneratorImpl.AUTO_INCREMENT_BATCH_SIZE);
        query.addScalar("next_id", LongType.INSTANCE);
        nextId = query.uniqueResult();

        session.close();

        return nextId;
    }

    @PreDestroy
    protected void destroy() {
        ObjectUtils.doIf(sessionFactory::close, sessionFactory.isOpen());
    }
}