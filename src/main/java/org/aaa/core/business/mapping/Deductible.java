package org.aaa.core.business.mapping;

import org.aaa.core.business.mapping.damage.Damage;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

import org.hibernate.annotations.Cache;

import javax.persistence.*;
import javax.persistence.Entity;
import java.io.Serializable;

/**
 * Created by alexandremasanes on 29/03/2017.
 */

@SuppressWarnings("unused")
@Entity
@Immutable
@Table(name = "deductibles")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Deductible implements Serializable {

    @Id
    @OneToOne
    @JoinColumn(
            name                 = "damage_id",
            referencedColumnName = "id"
    ) private Damage damage;

    @Column(
    ) private float value;

    @ManyToOne
    @JoinColumn(
            name                 = "insurance_id",
            referencedColumnName = "id"
    ) private Insurance insurance;

    public float getValue() {
        return value;
    }

    public Insurance getInsurance() {
        return insurance;
    }

    public Damage getDamage() {
        return damage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Deductible that = (Deductible) o;

        return damage != null ? damage.equals(that.damage) : that.damage == null;
    }

    @Override
    public int hashCode() {
        return damage != null ? damage.hashCode() : 0;
    }
}
