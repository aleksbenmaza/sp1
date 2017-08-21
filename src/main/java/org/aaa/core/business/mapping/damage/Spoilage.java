package org.aaa.core.business.mapping.damage;

import org.aaa.core.business.mapping.CarDealer;
import org.aaa.core.business.mapping.sinister.Sinister;

import javax.persistence.*;
import javax.persistence.Entity;

/**
 * Created by alexandremasanes on 20/03/2017.
 */
@Entity
@Table(name = "spoilages")
public class Spoilage extends Damage {

    @Column()
    private float rate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "car_dealer_id", referencedColumnName = "id")
    private CarDealer carDealer;

    public Spoilage(Sinister sinister) {
        super(sinister);
    }

    public Spoilage(
            Sinister sinister,
            CarDealer carDealer
    ) {
        this(sinister);
        setCarDealer(carDealer);
    }

    public CarDealer getCarDealer() {
        return carDealer;
    }

    public void setCarDealer(CarDealer carDealer) {
        this.carDealer = org.aaa.core.business.mapping.Entity.requireNonNull(carDealer);
        carDealer.addSpoilage(this);
    }

    Spoilage() {}
}