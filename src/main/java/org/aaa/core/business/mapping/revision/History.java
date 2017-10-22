package org.aaa.core.business.mapping.revision;

import org.aaa.orm.entity.identifiable.IdentifiableById;
import org.aaa.orm.entity.ImmutableEntity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;

/**
 * Created by alexandremasanes on 23/04/2017.
 */

@MappedSuperclass
public abstract class History extends ImmutableEntity implements IdentifiableById {

    @Id @GeneratedValue
    protected long id;

    @GeneratedValue @Column(name = "created_at", updatable = false, insertable = false)
    protected Timestamp createdAt;

    @Override
    public final long getId() {
        return id;
    }

    @Override
    public final int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    History() {}
}
