package org.aaa.core.business.mapping.sinister.accident;

import org.aaa.core.business.mapping.Contract;
import org.aaa.core.business.mapping.Vehicle;

import javax.persistence.*;
import javax.persistence.Entity;

/**
 * Created by alexandremasanes on 19/08/2017.
 */
@Entity
@Table(name = "third_party_accidents")
public class ThirdPartyAccident extends Accident {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    private Vehicle vehicle;

    public ThirdPartyAccident(
            Contract contract,
            Vehicle vehicle
    ) {
        super(contract);
        this.vehicle = org.aaa.core.business.mapping.Entity.requireNonNull(vehicle);
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    ThirdPartyAccident() {}
}
