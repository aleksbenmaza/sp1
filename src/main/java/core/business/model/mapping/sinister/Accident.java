package core.business.model.mapping.sinister;

import core.business.model.mapping.Vehicle;

import javax.persistence.*;

/**
 * Created by alexandremasanes on 20/03/2017.
 */
@Entity
@Table(name = "accidents")
public class Accident extends Sinister {

    @Column(name = "taux_resp")
    private float responsibilityRate;

    @Column(name = "pertes_humaines")
    private short casualtiesCount;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name            = "accidents_accidents",
            joinColumns        = {@JoinColumn(name                 = "id_0",
                                              referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name                 = "id_1",
                                              referencedColumnName = "id")})
    private Accident accident;

    public Accident(Vehicle vehicle) {
        super(vehicle);
    }

    public float getResponsibilityRate() {
        return responsibilityRate;
    }

    public void setResponsibilityRate(float responsibilityRate) {
        this.responsibilityRate = responsibilityRate;
    }

    public Accident getAccident() {
        return accident;
    }

    public void setAccident(Accident accident) {
        accident.accident = this;
        this.accident     = accident;
    }

    public short getCasualtiesCount() {
        return casualtiesCount;
    }

    public void setCasualtiesCount(short casualtiesCount) {
        this.casualtiesCount = casualtiesCount;
    }

    Accident() {
    }
}