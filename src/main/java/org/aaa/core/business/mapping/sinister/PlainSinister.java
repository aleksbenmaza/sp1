package org.aaa.core.business.mapping.sinister;

import org.aaa.core.business.mapping.*;

import javax.persistence.*;
import javax.persistence.Entity;

/**
 * Created by alexandremasanes on 20/03/2017.
 */
@Entity
@Table(name = "plain_sinisters")
public class PlainSinister extends Sinister {

    @ManyToOne(
            cascade = CascadeType.ALL
    ) @JoinColumn(
            name                 = "plain_sinister_type_id",
            referencedColumnName = "id"
    ) private PlainSinisterType type;

    public PlainSinister(Contract contract, PlainSinisterType type) {
        super(contract);
        this.type = requireNonNull(type);
        type.getPlainSinisters().add(this);
    }

    public PlainSinisterType getType() {
        return type;
    }

    PlainSinister() {}
}