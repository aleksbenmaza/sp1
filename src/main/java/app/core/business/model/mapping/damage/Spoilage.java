package app.core.business.model.mapping.damage;

import app.core.business.model.mapping.CarDealer;
import app.core.business.model.mapping.sinister.Sinister;

import javax.persistence.*;

/**
 * Created by alexandremasanes on 20/03/2017.
 */
@Entity
@Table(name = "deteriorations")
public class Spoilage extends Damage {

    @Column(name = "taux")
    private float rate;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "garagiste_id", referencedColumnName = "id")
    private CarDealer carDealer;

    public Spoilage(Sinister sinister, CarDealer carDealer) {
        super(sinister);
        setCarDealer(carDealer);
    }

    public CarDealer getCarDealer() {
        return carDealer;
    }

    public void setCarDealer(CarDealer carDealer) {
        carDealer.addSpoilage(this);
        this.carDealer = carDealer;
    }
}
