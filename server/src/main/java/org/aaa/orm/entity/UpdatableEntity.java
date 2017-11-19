package org.aaa.orm.entity;

import org.aaa.orm.entity.BaseEntity;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;

/**
 * Created by alexandremasanes on 23/09/2017.
 */

@MappedSuperclass
public abstract class UpdatableEntity extends BaseEntity {

    private static final long serialVersionUID = -4512272703014018308L;

    @Generated(GenerationTime.ALWAYS)
    @Column(name = "updated_at", insertable = false, updatable = false)
    protected Timestamp updateTime;

    public Timestamp getUpdateTime() {
        return updateTime;
    }
}
