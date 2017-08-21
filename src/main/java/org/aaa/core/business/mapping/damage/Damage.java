package org.aaa.core.business.mapping.damage;


import org.aaa.core.business.mapping.Deductible;
import org.aaa.core.business.mapping.sinister.Sinister;

import javax.persistence.*;

import javax.persistence.Entity;

import java.io.Serializable;

/**
 * Created by alexandremasanes on 30/01/2017.
 */

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) // + embeddedId = MESS
public abstract class Damage extends org.aaa.core.business.mapping.Entity implements Serializable {

    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "id")
    protected Sinister sinister;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "damage")
    protected Deductible deductible;

    @Column
    protected String description;

    @Column
    protected float amount;

    public Damage(Sinister sinister) {
        this.sinister = requireNonNull(sinister);
        sinister.setDamage(this);
    }

    public Sinister getSinister() {
        return sinister;
    }

    public Deductible getDeductible() {
        return deductible;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Damage damage = (Damage) o;

        return sinister.equals(damage.sinister);
    }

    @Override
    public int hashCode() {
        return sinister.hashCode();
    }

    Damage() {}
}