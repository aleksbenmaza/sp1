package org.aaa.core.business.mapping;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by alexandremasanes on 09/09/2017.
 */

@Embeddable
public class Ownership implements Serializable {

    @Column(name = "registration_number", unique = true)
    private String registrationNumber;

    @Column(name = "purchase_date")
    private Date purchaseDate;

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}
