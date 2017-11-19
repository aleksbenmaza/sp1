package org.aaa.core.business.mapping.entity;

import java.io.Serializable;

import org.aaa.core.business.mapping.entity.damage.Damage;

import org.aaa.orm.entity.BaseEntity;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import javax.persistence.Entity;

/**
 * Created by alexandremasanes on 29/03/2017.
 */

@Entity
@Immutable
@Table(name = "deductibles")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Deductible extends BaseEntity {

    private static final long serialVersionUID = 6299615088696914187L;

    @Embeddable
    public static class Id implements Serializable {

        private static final long serialVersionUID = -2618769407490295129L;

        @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(
                name                 = "damage_id",
                referencedColumnName = "id"
        ) private Damage damage;

        public Damage getDamage() {
            return damage;
        }

        public void setDamage(Damage damage) {
            this.damage = damage;
        }

        @Override
        public boolean equals(Object o) {
            return o == this || o instanceof Deductible && o.hashCode() == this.hashCode();
        }

        @Override
        public int hashCode() {
            return damage.hashCode();
        }

        Id() {}
    }

    @EmbeddedId
    private Id id;

    @Column(
    ) private float value;

    public float getValue() {
        return value;
    }

    public final Id getId() {
        return id;
    }

    @Override
    public final boolean equals(Object o) {
        return o instanceof Deductible && ((Deductible) o).id == this.id;
    }

    @Override
    public final int hashCode() {
        return id.hashCode();
    }

    private Deductible() {}
}