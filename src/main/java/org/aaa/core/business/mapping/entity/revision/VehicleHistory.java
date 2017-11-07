package org.aaa.core.business.mapping.entity.revision;

import org.aaa.core.business.mapping.entity.Contract;
import org.aaa.core.business.mapping.entity.Vehicle;
import org.aaa.core.business.mapping.entity.person.insuree.Insuree;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by alexandremasanes on 23/04/2017.
 */
@Entity
@Table(name = "h_vehicules")
public class VehicleHistory extends History {

    private static final long serialVersionUID = 7125845232269751210L;

    @Column
    private float value;

    @Column(name = "purchase_date")
    private Date purchaseDate;

    @Column(name = "registration_number", unique = true)
    private String registrationNumber;

    @Column(name = "registration_document")
    private boolean registrationDocumentPresent;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id", nullable = false)
    private Vehicle vehicle;

    @OneToOne
    @JoinColumn(name = "insuree_id", referencedColumnName = "id")
    private Insuree insuree;

    @OneToOne(mappedBy = "vehicle")
    @JoinColumn(name = "contract_id", referencedColumnName = "id", nullable = false)
    private Contract contract;

    @Override
    public final boolean equals(Object o) {
        return o instanceof VehicleHistory && this.id == ((VehicleHistory) o).getId();
    }

    public float getValue() {
        return value;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public boolean isRegistrationDocumentPresent() {
        return registrationDocumentPresent;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public Insuree getInsuree() {
        return insuree;
    }

    public Contract getContract() {
        return contract;
    }
}