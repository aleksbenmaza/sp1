package org.aaa.core.business.mapping.entity.sinister.accident;

import org.aaa.core.business.mapping.entity.Contract;
import org.aaa.core.business.mapping.entity.sinister.Sinister;

import javax.persistence.*;

/**
 * Created by alexandremasanes on 20/03/2017.
 */
@Entity
@Table(name = "accidents")
public abstract class Accident extends Sinister {

    private static final long serialVersionUID = 2941179199148322392L;

    @Column(
            name = "responsibility_rate"
    ) private float responsibilityRate;

    @Column(
            name = "casualties_count"
    ) private short casualtiesCount;


    public Accident(Contract contract) {
        super(contract);
    }

    public float getResponsibilityRate() {
        return responsibilityRate;
    }

    public void setResponsibilityRate(float responsibilityRate) {
        this.responsibilityRate = responsibilityRate;
    }

    public short getCasualtiesCount() {
        return casualtiesCount;
    }

    public void setCasualtiesCount(short casualtiesCount) {
        this.casualtiesCount = casualtiesCount;
    }

    Accident() {}
}