package org.aaa.core.business.mapping.entity.sinister.accident;

import org.aaa.orm.entity.identifiable.IdentifiedByIdEntity;
import org.aaa.core.business.mapping.entity.Contract;
import org.aaa.core.business.mapping.entity.Vehicle;

import javax.persistence.*;
import javax.persistence.Entity;
import java.sql.Time;
import java.util.Date;

/**
 * Created by alexandremasanes on 19/08/2017.
 */
@Entity
@Table(name = "third_party_accidents")
public class WithThirdPartyAccident extends Accident {

    private static final long serialVersionUID = 1463402185746234389L;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    private Vehicle vehicle;

    public WithThirdPartyAccident(
            Date date,
            Time time,
            Contract contract,
            Vehicle vehicle
    ) {
        super(date, time, contract);
        this.vehicle = IdentifiedByIdEntity.requireNonNull(vehicle);
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    WithThirdPartyAccident() {}
}
