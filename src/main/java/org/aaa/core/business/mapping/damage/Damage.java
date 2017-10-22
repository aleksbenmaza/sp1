package org.aaa.core.business.mapping.damage;

import java.io.Serializable;

import org.aaa.core.business.mapping.Deductible;
import org.aaa.orm.entity.UpdatableEntity;
import org.aaa.core.business.mapping.sinister.Sinister;

import javax.persistence.*;

import javax.persistence.Entity;

/**
 * Created by alexandremasanes on 30/01/2017.
 */

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) // + embeddedId = MESS
public abstract class Damage extends UpdatableEntity {

    @Embeddable
    public static class Id implements Serializable {

        @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "id", referencedColumnName = "id")
        private Sinister sinister;

        public Id(Sinister sinister) {
            this.sinister = sinister;
            check(requireNonNull(sinister).getDamage() == null);
        }

        public Sinister getSinister() {
            return sinister;
        }

        @Override
        public boolean equals(Object o) {
           return this == o || o instanceof Id && this.hashCode() == o.hashCode();
        }

        @Override
        public int hashCode() {
            return sinister.hashCode();
        }

        Id() {}
    }

    @EmbeddedId
    private Id id;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "id.damage")
    protected Deductible deductible;

    @Column
    protected String description;

    @Column
    protected float amount;

    public Damage(Id id) {
        this.id = id;
        id.sinister.setDamage(this);
    }

    public final Id getSinister() {
        return id;
    }

    public Deductible getDeductible() {
        return deductible;
    }

    @Override
    public final boolean equals(Object o) {
        return o instanceof Damage && this.hashCode() == o.hashCode();
    }

    @Override
    public final int hashCode() {
        return id.hashCode();
    }

    Damage() {}
}