package org.aaa.core.business.mapping.revision;

import org.aaa.core.business.mapping.IdentifiableById;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;

/**
 * Created by alexandremasanes on 23/04/2017.
 */
@Immutable
@MappedSuperclass
public abstract class History implements IdentifiableById {

    @Id @GeneratedValue
    protected Long id;

    @GeneratedValue @Column(name = "created_at")
    protected Timestamp createdAt;

    @Override
    public long getId() {
        return id;
    }

    History() {
    }
}
