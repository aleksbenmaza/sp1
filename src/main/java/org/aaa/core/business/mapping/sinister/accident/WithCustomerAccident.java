package org.aaa.core.business.mapping.sinister.accident;

import org.aaa.core.business.mapping.Contract;

import javax.persistence.*;

/**
 * Created by alexandremasanes on 19/08/2017.
 */
@Entity
@Table(name = "customer_accidents")
public class CustomerAccident extends Accident {

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
    ) private CustomerAccident accident;

    public CustomerAccident(Contract contract) {
        super(contract);
    }

    public Accident getAccident() {
        return accident;
    }

    public void setAccident(CustomerAccident accident) {
        accident.accident = this;
        this.accident     = accident;
    }

    CustomerAccident() {}
}