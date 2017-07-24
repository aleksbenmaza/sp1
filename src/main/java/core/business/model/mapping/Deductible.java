package app.core.business.model.mapping;

import app.core.business.model.mapping.damage.Damage;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import javax.persistence.Entity;
import java.io.Serializable;

/**
 * Created by alexandremasanes on 29/03/2017.
 */

@Immutable
@Entity
@Table(name = "franchises")
public class Deductible implements Serializable {

    @Id
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dommage_id", referencedColumnName = "id")
    private Damage damage;

    @Column(name = "valeur")
    private float value;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_garantie_id", referencedColumnName = "id")
    private Insurance insurance;

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
