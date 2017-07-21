package app.core.business.model.mapping;

import javax.persistence.*;

/**
 * Created by alexandremasanes on 21/07/2017.
 */
@MappedSuperclass
public abstract class IdentifiableByIdImpl extends Entity implements IdentifiableById {

    @Id
    @GeneratedValue
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

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}