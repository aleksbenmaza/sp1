package org.aaa.core.business.mapping;

import javax.persistence.*;

/**
 * Created by alexandremasanes on 21/07/2017.
 */
@MappedSuperclass
public abstract class IdentifiableByIdImpl extends org.aaa.core.business.mapping.Entity implements IdentifiableById {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY) //allows calling getId without loading whole proxy
    private long id;

    @Override
    public long getId() {
        return id;
    }

    //needed for property access, set to private for security reason
    private void setId(long id) {
        this.id = id;
    }


    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}