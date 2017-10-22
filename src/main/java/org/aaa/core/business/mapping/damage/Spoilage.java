package org.aaa.core.business.mapping.damage;

import org.aaa.orm.entity.identifiable.IdentifiedByIdEntity;
import org.aaa.core.business.mapping.CarDealer;

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

    public Spoilage(Id id) {
        super(id);
    }

    public Spoilage(
            Id        id,
            CarDealer carDealer
    ) {
        this(id);
        setCarDealer(carDealer);
    }

    public CarDealer getCarDealer() {
        return carDealer;
    }

    public void setCarDealer(CarDealer carDealer) {
        this.carDealer = IdentifiedByIdEntity.requireNonNull(carDealer);
        carDealer.addSpoilage(this);
    }

    Spoilage() {}
}