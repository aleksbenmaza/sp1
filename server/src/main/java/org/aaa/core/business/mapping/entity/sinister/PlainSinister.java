package org.aaa.core.business.mapping.entity.sinister;

import org.aaa.core.business.mapping.entity.Contract;
import org.aaa.core.business.mapping.entity.PlainSinisterType;

import javax.persistence.*;
import javax.persistence.Entity;
import java.sql.Time;
import java.util.Date;

/**
 * Created by alexandremasanes on 20/03/2017.
 */
@Entity
@Table(name = "plain_sinisters")
public class PlainSinister extends Sinister {

    private static final long serialVersionUID = -1787070553375003163L;

    @ManyToOne(
            cascade = CascadeType.ALL
    ) @JoinColumn(
            name                 = "plain_sinister_type_id",
            referencedColumnName = "id"
    ) private PlainSinisterType type;

    public PlainSinister(Date date, Time time, Contract contract, PlainSinisterType type) {
        super(date, time, contract);
        this.type = requireNonNull(type);
        type.getPlainSinisters().add(this);
    }

    public PlainSinisterType getType() {
        return type;
    }

    PlainSinister() {}
}