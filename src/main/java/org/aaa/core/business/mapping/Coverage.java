package org.aaa.core.business.mapping;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by alexandremasanes on 02/09/2017.
 */

@Embeddable
public class Coverage implements Serializable {

    private float rate;

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
