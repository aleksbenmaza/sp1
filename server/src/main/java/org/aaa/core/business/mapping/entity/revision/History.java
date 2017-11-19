package org.aaa.core.business.mapping.entity.revision;

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

    private static final long serialVersionUID = -5902443307699149633L;

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
        return Long.hashCode(id);
    }

    @Override
    public final boolean equals(Object that) {
        return that instanceof History
            && resolvedClass(this) == resolvedClass(((History)that))
            && this.id == ((History) that).id;
    }

    History() {}
}
