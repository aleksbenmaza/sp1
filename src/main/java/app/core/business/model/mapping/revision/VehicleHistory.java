package app.core.business.model.mapping.revision;

import app.core.business.model.mapping.Contract;
import app.core.business.model.mapping.Vehicle;
import app.core.business.model.mapping.person.insuree.Insuree;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by alexandremasanes on 23/04/2017.
 */
@Entity
@Table(name = "h_vehicules")
public class VehicleHistory extends History {

    @Column(name = "valeur")
    private float value;

    @Column(name = "date_achat")
    private Date purchaseDate;

    @Column(name = "immatriculation", unique = true)
    private String registrationNumber;

    @Column(name = "carte_grise")
    private String registrationFilePath;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicule_id", referencedColumnName = "id")
    private Vehicle vehicle;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "assure_id", referencedColumnName = "id")
    private Insuree insuree;

    @OneToOne(mappedBy = "vehicle", fetch = FetchType.EAGER)
    @JoinColumn(name = "contrat_id", referencedColumnName = "id")
    private Contract contract;

    public float getValue() {
        return value;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getRegistrationFilePath() {
        return registrationFilePath;
    }

    public Insuree getInsuree() {
        return insuree;
    }

    public Contract getContract() {
        return contract;
    }
}