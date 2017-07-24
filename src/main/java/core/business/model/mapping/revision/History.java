package app.core.business.model.mapping.revision;

import app.core.business.model.mapping.IdentifiableById;
import org.hibernate.annotations.Immutable;

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

    @GeneratedValue
    protected Timestamp createdAt;

    @Override
    public long getId() {
        return id;
    }

    History() {
    }
}
