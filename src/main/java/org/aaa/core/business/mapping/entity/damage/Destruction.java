package org.aaa.core.business.mapping.entity.damage;

import javax.persistence.*;

/**
 * Created by alexandremasanes on 20/03/2017.
 */
@Entity
@Table(name = "destructions")
public class Destruction extends Damage {

    private static final long serialVersionUID = -935253888954278062L;

    @Column(name = "is_total")
    private boolean total;

    public Destruction(Id id) {
        super(id);
    }

    public boolean isTotal() {
        return total;
    }

    public void setTotal(boolean total) {
        this.total = total;
    }

    Destruction() {}
}