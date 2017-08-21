package org.aaa.core.business.mapping.damage;

import org.aaa.core.business.mapping.sinister.Sinister;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by alexandremasanes on 20/03/2017.
 */
@Entity
@Table(name = "destructions")
public class Destruction extends Damage {

    @Column(name = "is_total")
    private boolean total;

    public Destruction(Sinister sinister) {
        super(sinister);
    }

    public boolean isTotal() {
        return total;
    }

    public void setTotal(boolean total) {
        this.total = total;
    }

    Destruction() {}
}