package org.aaa.core.business.mapping;

import javax.persistence.*;

/**
 * Created by alexandremasanes on 21/07/2017.
 */
@MappedSuperclass
public abstract class IdentifiableByIdImpl extends org.aaa.core.business.mapping.Entity implements IdentifiableById {

    @Id
    @Access(AccessType.PROPERTY) //allows calling getId without loading whole proxy
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Override
    public long getId() {
        return id;
    }

    //needed for property access, set to private for security reason
    private void setId(long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public boolean equals(Object o) {
        return o == this
                || (o != null
                && o.getClass() == this.getClass()
                && IdentifiableById.super.equals(o)
        );
    }
}