package org.aaa.orm.entity.identifiable;


import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;

import javax.persistence.PrePersist;

/**
 * Created by alexandremasanes on 11/11/2017.
 */
public class IdentifiableByEntityListener implements PreInsertEventListener {

    private static final long serialVersionUID = -852775568788121851L;

    @Override
    public boolean onPreInsert(PreInsertEvent preInsertEvent) {
        if(preInsertEvent.getEntity() instanceof IdentifiedByIdEntity)
            IdGenerator.injectNextId((IdentifiedByIdEntity) preInsertEvent.getEntity());
        return true;
    }

    @PrePersist
    public void beforePersist(IdentifiedByIdEntity entity) {
        System.err.println("toto");
        IdGenerator.injectNextId(entity);
    }
}