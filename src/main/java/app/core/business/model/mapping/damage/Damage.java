package app.core.business.model.mapping.damage;


import app.core.business.model.mapping.Deductible;
import app.core.business.model.mapping.sinister.Sinister;

import javax.persistence.*;

import javax.persistence.Entity;

import java.io.Serializable;

/**
 * Created by alexandremasanes on 30/01/2017.
 */

@Entity
@Table(name = "dommages")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) // + embeddedId = MESS
public abstract class Damage extends app.core.business.model.mapping.Entity implements Serializable {

    @Embeddable
    public static class Id implements Serializable {

        @OneToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "id", referencedColumnName = "id")
        protected Sinister sinister;

        public Id(Sinister sinister) {
            this.sinister = requireNonNull(sinister);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Id id = (Id) o;

            return sinister.equals(id.sinister);
        }

        @Override
        public int hashCode() {
            return sinister.hashCode();
        }

        Id() {

        }
    }

    @EmbeddedId
    protected Id id;

    @OneToOne(fetch = FetchType.EAGER)
    protected transient Deductible deductible;

    protected String description;

    @Column(name = "montant")
    protected float amount;

    public Damage(Sinister sinister) {
        if(sinister.getDamage() != null)
            throw new BusinessException();
        this.id = new Id(sinister);
        sinister.setDamage(this);
    }

    public Sinister getSinister() {
        return id.sinister;
    }

    public Deductible getDeductible() {
        return deductible;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Damage damage = (Damage) o;

        return id.equals(damage.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    Damage() {

    }
}