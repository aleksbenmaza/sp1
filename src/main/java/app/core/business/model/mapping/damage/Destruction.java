package app.core.business.model.mapping.damage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by alexandremasanes on 20/03/2017.
 */
@Entity
@Table(name = "destructions")
public class Destruction extends Damage {

    @Column(name = "est_total")
    private boolean total;

    public boolean isTotal() {
        return total;
    }

    public void setTotal(boolean total) {
        this.total = total;
    }
}