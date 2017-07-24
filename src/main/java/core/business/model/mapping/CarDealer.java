package app.core.business.model.mapping;

import app.core.business.model.mapping.damage.Spoilage;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by alexandremasanes on 20/03/2017.
 */

@Entity
@Table(name = "garagistes")
public class CarDealer extends IdentifiableByIdImpl {


    @Column(name = "nom")
    private String name;

    @Column(name = "agree")
    private boolean certified;

    @OneToMany(mappedBy = "carDealer", cascade = CascadeType.ALL)
    private Set<Spoilage> spoilages;

    public CarDealer() {
        spoilages = new HashSet<Spoilage>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCertified() {
        return certified;
    }

    public void setCertified(boolean certified) {
        this.certified = certified;
    }

    public Set<Spoilage> getSpoilages() {
        return new HashSet<Spoilage>(spoilages);
    }

    public boolean addSpoilage(Spoilage spoilage) {
        return spoilages.add(spoilage);
    }
}