package org.aaa.core.business.mapping.embeddable;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by alexandremasanes on 02/09/2017.
 */

@Embeddable
public class Coverage implements Serializable {

    private static final long serialVersionUID = 8228927396664841524L;

    private float rate;

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
