package org.aaa.core.business.mapping.entity.sinister.accident;

import org.aaa.core.business.mapping.entity.Contract;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

/**
 * Created by alexandremasanes on 19/08/2017.
 */
@Entity
@Table(name = "customer_accidents")
public class WithCustomerAccident extends Accident {

    private static final long serialVersionUID = -4679156685945571234L;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(
            name            = "accidents__accidents",
            joinColumns     = @JoinColumn(
                    name                 = "id_0",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name                 = "id_1",
                    referencedColumnName = "id"
            )
    ) private WithCustomerAccident accident;

    public WithCustomerAccident(Date date, Time time, Contract contract) {
        super(date, time, contract);
    }

    public Accident getAccident() {
        return accident;
    }

    public void setAccident(WithCustomerAccident accident) {
        accident.accident = this;
        this.accident     = accident;
    }

    WithCustomerAccident() {}
}